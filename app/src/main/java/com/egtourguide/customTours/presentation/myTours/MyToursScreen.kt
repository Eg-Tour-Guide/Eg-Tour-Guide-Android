package com.egtourguide.customTours.presentation.myTours

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
import com.egtourguide.core.presentation.components.DataScreenHeader
import com.egtourguide.core.utils.ItemType
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.PullToRefreshScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.filter.FilterScreenViewModel

@Preview
@Composable
private fun MyToursScreenPreview() {
    EGTourGuideTheme {
        MyToursScreenContent(
            uiState = MyToursUIState(
                isLoading = false,
                myTours = (0..3).map {
                    AbstractedTour(
                        id = "$it",
                        title = "Test $it",
                        isSaved = true,
                        image = "",
                        duration = 3,
                        ratingCount = 5,
                        rating = 3.5
                    )
                },
                displayedTours = (0..3).map {
                    AbstractedTour(
                        id = "$it",
                        title = "Test $it",
                        isSaved = true,
                        image = "",
                        duration = 3,
                        ratingCount = 5,
                        rating = 3.5
                    )
                }
            )
        )
    }
}

@Composable
fun MyToursScreen(
    viewModel: MyToursViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    onNavigateToCreateTour: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSingleTour: (AbstractedTour) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterMyTours(filterState)
    }

    PullToRefreshScreen(
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshMyTours
    ) {
        MyToursScreenContent(
            uiState = uiState,
            hasChanged = hasChanged,
            onFilterClicked = onNavigateToFilters,
            onTourClicked = onNavigateToSingleTour,
            onSaveClicked = viewModel::onSaveClicked,
            onAddClicked = onNavigateToCreateTour,
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
                viewModel.getMyTours()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
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
private fun MyToursScreenContent(
    uiState: MyToursUIState = MyToursUIState(),
    hasChanged: Boolean = false,
    onFilterClicked: () -> Unit = {},
    onTourClicked: (AbstractedTour) -> Unit = {},
    onSaveClicked: (AbstractedTour) -> Unit = {},
    onAddClicked: () -> Unit = {},
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
            showAdd = (!uiState.isNetworkError && !uiState.isLoading),
            onBackClicked = onBackClicked,
            onAddClicked = onAddClicked
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
                    title = stringResource(id = R.string.tours_count, uiState.displayedTours.size),
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                if (uiState.displayedTours.isEmpty()) {
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
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(items = uiState.displayedTours, key = { it.id }) { tour ->
                            LargeCard(
                                itemType = ItemType.TOUR,
                                image = tour.image,
                                name = tour.title,
                                isSaved = tour.isSaved,
                                duration = tour.duration,
                                ratingAverage = tour.rating,
                                ratingCount = tour.ratingCount,
                                onItemClicked = { onTourClicked(tour) },
                                onSaveClicked = { onSaveClicked(tour) }
                            )
                        }
                    }
                }
            }
        }
    }
}