package com.egtourguide.user.presentation.saved

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.egtourguide.R
import com.egtourguide.core.utils.ItemType
import com.egtourguide.core.presentation.components.DataScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.user.domain.model.AbstractSavedItem
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.PullToRefreshScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.filter.FilterScreenViewModel

@Preview
@Composable
private fun SavedScreenPreview() {
    EGTourGuideTheme {
        SavedScreenContent(
            uiState = SavedScreenUIState(
                isLoading = false,
                savedList = (1..5).map {
                    AbstractSavedItem(
                        id = "$it",
                        image = "",
                        name = "Test",
                        itemType = ItemType.LANDMARK
                    )
                },
                displayedSavedList = (1..5).map {
                    AbstractSavedItem(
                        id = "$it",
                        image = "",
                        name = "Test",
                        itemType = ItemType.LANDMARK
                    )
                }
            )
        )
    }
}

@Composable
fun SavedScreen(
    viewModel: SavedViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSingleItem: (AbstractSavedItem) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterSavedItems(filterState)
    }

    PullToRefreshScreen(
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshSaved
    ) {
        SavedScreenContent(
            uiState = uiState,
            hasChanged = hasChanged,
            onFilterClicked = onNavigateToFilters,
            onSaveClicked = viewModel::onSaveClicked,
            onItemClicked = onNavigateToSingleItem,
            onBackClicked = onNavigateBack
        )
    }

    LaunchedEffect(key1 = uiState.refreshFilters) {
        if (uiState.refreshFilters) {
            filterViewModel.resetFilters()
            viewModel.whenFiltersRefreshed()
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.isCallSent) {
                viewModel.getSavedItems()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            val message = if (uiState.isSaveCall) R.string.save_success else R.string.unsave_success
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveSuccess()
        }
    }

    LaunchedEffect(key1 = uiState.isSaveError) {
        if (uiState.isSaveError) {
            val message = if (uiState.isSaveCall) R.string.save_error else R.string.unsave_error
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveError()
        }
    }
}

@Composable
private fun SavedScreenContent(
    uiState: SavedScreenUIState = SavedScreenUIState(),
    hasChanged: Boolean = false,
    onFilterClicked: () -> Unit = {},
    onItemClicked: (AbstractSavedItem) -> Unit = {},
    onSaveClicked: (AbstractSavedItem) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(52.dp),
            showBack = true,
            onBackClicked = onBackClicked
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
            NetworkErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                DataScreenHeader(
                    title = stringResource(
                        id = R.string.saved_count,
                        uiState.displayedSavedList.size
                    ),
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                if (uiState.displayedSavedList.isEmpty()) {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        items(items = uiState.displayedSavedList, key = { it.id }) { item ->
                            LargeCard(
                                itemType = item.itemType,
                                image = item.image,
                                name = item.name,
                                isSaved = item.isSaved,
                                duration = item.duration,
                                location = item.location,
                                ratingAverage = item.ratingAverage,
                                ratingCount = item.ratingCount,
                                artifactType = item.artifactType,
                                onItemClicked = { onItemClicked(item) },
                                onSaveClicked = { onSaveClicked(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}