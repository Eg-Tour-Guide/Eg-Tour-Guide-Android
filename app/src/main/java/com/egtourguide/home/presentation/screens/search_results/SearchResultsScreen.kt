package com.egtourguide.home.presentation.screens.search_results


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.EmptyState
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.ScreenHeader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchResultsScreen(
    viewModel: SearchResultsViewModel = hiltViewModel(),
    query: String = "",
    filters: HashMap<*, *>? = null,
    selectedBottomBarItem: BottomBarScreens = BottomBarScreens.Home,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSingleItem: (SearchResult) -> Unit = {},
    onNavigateToLandmarks: () -> Unit = {},
    onNavigateToTours: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToArtifacts: () -> Unit = {},
    onNavigateToUser: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Log.d("````TAG````", "filters in screen: $filters")
    viewModel.filters = filters

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedScreen = selectedBottomBarItem
            ) { selectedScreen ->
                when (selectedScreen) {
                    BottomBarScreens.Home -> onNavigateToHome()
                    BottomBarScreens.Tours -> onNavigateToTours()
                    BottomBarScreens.Landmarks -> onNavigateToLandmarks()
                    BottomBarScreens.Artifacts -> onNavigateToArtifacts()
                    BottomBarScreens.User -> onNavigateToUser()
                }
            }
        }
    ) {
        SearchResultsScreenContent(
            uiState = uiState,
            onSearchClicked = onNavigateToSearch,
            onFilterClicked = onNavigateToFilters,
            onResultClicked = onNavigateToSingleItem,
            onSaveClicked = viewModel::onSaveClicked
        )
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getSearchResults(query = query)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
    onSearchClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onResultClicked: (SearchResult) -> Unit,
    onSaveClicked: (SearchResult) -> Unit
) {
    Column(
        Modifier
            .padding(bottom = 60.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(62.dp),
            showLogo = true,
            showSearch = true,
            onSearchClicked = onSearchClicked
        )
        Spacer(modifier = Modifier.height(18.dp))
        ResultsHeader(
            resultsCount = uiState.results.size,
            onFilterClicked = onFilterClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }
        AnimatedVisibility(
            visible = uiState.isShowEmptyState && uiState.results.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(
                message = "No Results Found"
            )
        }
        AnimatedVisibility(
            visible = !uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ResultsSection(
                results = uiState.results,
                onResultClicked = onResultClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Composable
private fun ResultsHeader(
    resultsCount: Int,
    onFilterClicked: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$resultsCount Results",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        /*Icon(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onFilterClicked()
            },
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "Filter Icon",
            tint = Color.Unspecified
        )*/
    }
}

@Composable
private fun ResultsSection(
    results: List<SearchResult>,
    onResultClicked: (SearchResult) -> Unit,
    onSaveClicked: (SearchResult) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = results) { result ->
            ResultItem(
                item = result,
                onItemClicked = onResultClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Composable
fun ResultItem(
    item: SearchResult,
    onItemClicked: (SearchResult) -> Unit,
    onSaveClicked: (SearchResult) -> Unit
) {
    var isSaved by remember { mutableStateOf(item.isSaved) }
    Column(
        modifier = Modifier
            .width(144.dp)
            .height(205.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onItemClicked(item)
            }
    ) {
        MainImage(
            modifier = Modifier
                .height(105.dp)
                .clip(RoundedCornerShape(4.dp)),
            data = "${if (item.isArtifact) "artifacsImages/" else "placeImages/"}${item.image}"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.height(36.dp),
            text = item.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_location),
                    tint = Color.Unspecified,
                    contentDescription = "Location Icon"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(.90f),
                    text = " ${item.location}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    isSaved = !isSaved
                    onSaveClicked(item)
                }
            ) {
                Icon(
                    painter = painterResource(id = if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                    contentDescription = "Save Icon"
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (!item.isArtifact) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_rating_star),
                    tint = Color.Unspecified,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = "%.2f".format(item.rating),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.reviews_count, item.ratingCount ?: 0),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_artifacts_selected),
                    tint = Color.Unspecified,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = item.artifactType ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchResultsScreenPreview() {
    SearchResultsScreen()
}