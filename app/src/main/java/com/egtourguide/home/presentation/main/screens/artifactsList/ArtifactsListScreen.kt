package com.egtourguide.home.presentation.main.screens.artifactsList

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.core.presentation.components.DataScreenHeader
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.PullToRefreshScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.presentation.filter.FilterScreenViewModel
import com.egtourguide.home.presentation.main.components.ArtifactDetectionDialog

@Preview
@Composable
private fun ArtifactsScreenPreview() {
    EGTourGuideTheme {
        ArtifactsListScreenContent(
            hasChanged = true,
            uiState = ArtifactsListUIState(
                isLoading = false,
                isNetworkError = false,
                displayedArtifacts = (0..6).map {
                    AbstractedArtifact(
                        id = "$it",
                        name = "Yolanda Koch",
                        image = "",
                        isSaved = false,
                        type = "eam",
                        material = "Stone",
                        museumName = "Jeanie Norris"
                    )
                },
                artifacts = (0..9).map {
                    AbstractedArtifact(
                        id = "$it",
                        name = "Yolanda Koch",
                        image = "",
                        isSaved = false,
                        type = "eam",
                        material = "Stone",
                        museumName = "Jeanie Norris"
                    )
                }
            )
        )
    }
}

@Composable
fun ArtifactsListScreen(
    viewModel: ArtifactsListViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSingleArtifact: (AbstractedArtifact) -> Unit = {},
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterArtifacts(filterState)
    }

    var isDetectionDialogShown by remember { mutableStateOf(false) }

    PullToRefreshScreen(
        isRefreshing = uiState.isRefreshing,
        onRefresh = {
            viewModel.refreshArtifacts { artifactTypes, materials ->
                filterViewModel.setArtifactFilters(
                    artifactTypes = artifactTypes,
                    materials = materials
                )
            }
        }
    ) {
        ArtifactsListScreenContent(
            uiState = uiState,
            hasChanged = hasChanged,
            onSearchClicked = onNavigateToSearch,
            onFilterClicked = onNavigateToFilters,
            onArtifactClicked = onNavigateToSingleArtifact,
            onSaveClicked = viewModel::onSaveClicked,
            onCaptureObjectClicked = { isDetectionDialogShown = true }
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
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getArtifactsList { artifactTypes, materials ->
                    filterViewModel.setArtifactFilters(
                        artifactTypes = artifactTypes,
                        materials = materials
                    )
                }
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

    LaunchedEffect(key1 = uiState.detectedArtifact) {
        if (uiState.detectedArtifact != null) {
            isDetectionDialogShown = false
            onNavigateToDetectedArtifact(uiState.detectedArtifact!!)
            viewModel.clearDetectionSuccess()
            Toast.makeText(context, uiState.detectedArtifact!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = uiState.isDetectionError) {
        if (uiState.isDetectionError) {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_detect_please_try_again),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearDetectionError()
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
fun ArtifactsListScreenContent(
    uiState: ArtifactsListUIState,
    hasChanged: Boolean = false,
    onSearchClicked: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onArtifactClicked: (AbstractedArtifact) -> Unit = {},
    onSaveClicked: (AbstractedArtifact) -> Unit = {},
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
                        id = R.string.artifacts_count,
                        uiState.displayedArtifacts.size
                    ),
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                if (uiState.displayedArtifacts.isEmpty()) {
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
                        items(items = uiState.displayedArtifacts, key = { it.id }) { artifact ->
                            LargeCard(
                                itemType = ItemType.ARTIFACT,
                                image = artifact.image,
                                name = artifact.name,
                                isSaved = artifact.isSaved,
                                location = artifact.museumName,
                                artifactType = artifact.type,
                                onItemClicked = { onArtifactClicked(artifact) },
                                onSaveClicked = { onSaveClicked(artifact) }
                            )
                        }
                    }
                }
            }
        }
    }
}