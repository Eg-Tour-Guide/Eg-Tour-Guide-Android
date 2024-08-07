package com.egtourguide.home.presentation.searchResults

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.utils.ItemType
import com.egtourguide.core.presentation.components.DataScreenHeader
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.ExpandedType
import com.egtourguide.home.presentation.filter.FilterScreenViewModel

@Composable
fun SearchResultsScreen(
    viewModel: SearchResultsViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    query: String,
    onNavigateToSearch: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSingleItem: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterResults(filterState)
    }

    SearchResultsScreenContent(
        uiState = uiState,
        hasChanged = hasChanged,
        onSearchClicked = onNavigateToSearch,
        onFilterClicked = onNavigateToFilters,
        onResultClicked = onNavigateToSingleItem,
        onSaveClicked = viewModel::onSaveClicked
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.isCallSent) {
                viewModel.getSearchResults(query = query)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // TODO: Check this!!
    LaunchedEffect(key1 = uiState.isSaveSuccess, key2 = uiState.saveError) {
        val successMsg =
            if (uiState.isSave) "Place Saved Successfully" else "Place Unsaved Successfully"
        if (uiState.isSaveSuccess) {
            Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveSuccess()
        } else if (uiState.saveError != null) {
            Toast.makeText(
                context,
                "There are a problem in saving the place, try again later",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSaveError()
        }
    }
}

@Composable
fun SearchResultsScreenContent(
    uiState: SearchResultsUIState,
    hasChanged: Boolean = false,
    onSearchClicked: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onResultClicked: (String, String) -> Unit = { _, _ -> },
    onSaveClicked: (SearchResult) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(61.dp),
            showLogo = true,
            showSearch = true,
            onSearchClicked = onSearchClicked
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NetworkErrorScreen(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.results.isEmpty() && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.results.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                DataScreenHeader(
                    title = stringResource(id = R.string.results_count, uiState.results.size),
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ResultsSection(
                    results = uiState.results,
                    onResultClicked = onResultClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ResultsSection(
    results: List<SearchResult>,
    onResultClicked: (String, String) -> Unit,
    onSaveClicked: (SearchResult) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = results, key = { it.id }) { result ->
            LargeCard(
                itemType = result.itemType,
                image = result.image,
                name = result.name,
                isSaved = result.isSaved,
                location = result.location,
                ratingAverage = result.rating,
                ratingCount = result.ratingCount,
                artifactType = result.artifactType,
                onItemClicked = {
                    onResultClicked(
                        result.id,
                        if (result.itemType == ItemType.LANDMARK) ExpandedType.LANDMARK.name
                        else ExpandedType.ARTIFACT.name
                    )
                },
                onSaveClicked = { onSaveClicked(result) }
            )
        }
    }
}

@Preview
@Composable
private fun SearchResultsScreenPreview() {
    EGTourGuideTheme {
        SearchResultsScreenContent(
            hasChanged = true,
            uiState = SearchResultsUIState(
                isLoading = false,
                results = (0..4).map {
                    SearchResult(
                        id = "$it",
                        name = "John Johnson",
                        image = "pro",
                        location = "Cairo",
                        isSaved = false,
                        rating = 6.7,
                        ratingCount = 8388,
                        itemType = ItemType.LANDMARK,
                        artifactType = "Statue"
                    )
                }
            )
        )
    }
}