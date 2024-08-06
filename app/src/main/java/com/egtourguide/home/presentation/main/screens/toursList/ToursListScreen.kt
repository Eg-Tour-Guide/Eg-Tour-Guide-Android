package com.egtourguide.home.presentation.main.screens.toursList

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.presentation.filter.FilterScreenViewModel
import com.egtourguide.home.presentation.main.components.ArtifactDetectionDialog

@Composable
fun ToursListScreen(
    viewModel: ToursListViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSingleTour: (AbstractedTour) -> Unit,
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterTours(filterState)
    }

    var isDetectionDialogShown by remember { mutableStateOf(false) }

    ToursListScreenContent(
        uiState = uiState,
        hasChanged = hasChanged,
        onSearchClicked = onNavigateToSearch,
        onFilterClicked = onNavigateToFilters,
        onTourClicked = onNavigateToSingleTour,
        onSaveClicked = viewModel::onSaveClicked,
        onCaptureObjectClicked = { isDetectionDialogShown = true }
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getToursList()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // TODO: Check messages!!
    LaunchedEffect(key1 = uiState.isSaveSuccess, key2 = uiState.saveError) {
        val successMsg =
            if (uiState.isSave) "Tour Saved Successfully" else "Tour Unsaved Successfully"
        if (uiState.isSaveSuccess) {
            Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveSuccess()
        } else if (uiState.saveError != null) {
            Toast.makeText(
                context,
                "There are a problem in saving the Tour, try again later",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSaveError()
        }
    }

    LaunchedEffect(key1 = uiState.detectedArtifact) {
        if (uiState.detectedArtifact != null) {
            isDetectionDialogShown = false
            onNavigateToDetectedArtifact(uiState.detectedArtifact!!)
            viewModel.clearDetectionSuccess()
            Toast.makeText(context, uiState.detectedArtifact!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    ArtifactDetectionDialog(
        isShown = isDetectionDialogShown,
        isDetectionLoading = uiState.isDetectionLoading,
        onDismissRequest = { isDetectionDialogShown = false },
        detectArtifact = viewModel::detectArtifact
    )
}

@Composable
fun ToursListScreenContent(
    uiState: ToursListUIState = ToursListUIState(),
    hasChanged: Boolean = false,
    onSearchClicked: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onTourClicked: (AbstractedTour) -> Unit = {},
    onSaveClicked: (AbstractedTour) -> Unit = {},
    onCaptureObjectClicked: () -> Unit = {}
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
            showCaptureObject = true,
            onSearchClicked = onSearchClicked,
            onCaptureObjectClicked = onCaptureObjectClicked
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.tours.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(id = R.string.no_tours_found)
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.tours.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                DataScreenHeader(
                    title = stringResource(
                        id = R.string.tours_count,
                        uiState.displayedTours.size
                    ),
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ToursSection(
                    tours = uiState.displayedTours,
                    onTourClicked = onTourClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ToursSection(
    tours: List<AbstractedTour>,
    onTourClicked: (AbstractedTour) -> Unit,
    onSaveClicked: (AbstractedTour) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = tours, key = { it.id }) { tour ->
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

@Preview
@Composable
private fun ToursScreenPreview() {
    EGTourGuideTheme {
        ToursListScreenContent(
            hasChanged = false,
            uiState = ToursListUIState(
                isLoading = false,
//                displayedTours = emptyList(),
                displayedTours = (0..3).map {
                    AbstractedTour(
                        id = "$it",
                        image = "0",
                        title = "eros",
                        duration = 3645,
                        rating = 10.11,
                        ratingCount = 3276,
                        isSaved = false
                    )
                },
                tours = (0..9).map {
                    AbstractedTour(
                        id = "$it",
                        image = "0",
                        title = "eros",
                        duration = 3645,
                        rating = 10.11,
                        ratingCount = 3276,
                        isSaved = false
                    )
                },
            )
        )
    }
}