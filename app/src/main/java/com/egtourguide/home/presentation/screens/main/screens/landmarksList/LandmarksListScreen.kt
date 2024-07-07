package com.egtourguide.home.presentation.screens.main.screens.landmarksList

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.LandmarkItem
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.presentation.screens.filter.FilterScreenViewModel
import com.egtourguide.home.presentation.screens.main.components.ArtifactDetectionDialog

@Composable
fun LandmarksListScreen(
    viewModel: LandmarksListViewModel = hiltViewModel(),
    filterViewModel: FilterScreenViewModel,
    onNavigateToSearch: () -> Unit = {},
    navigateToNotifications: () -> Unit,
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSinglePlace: (AbstractedLandmark) -> Unit = {},
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by filterViewModel.uiState.collectAsState()
    val hasChanged by filterViewModel.hasChanged.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = filterState) {
        viewModel.filterLandmarks(filterState)
    }

    var isArtifactDetectionDialogShown by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    val galleryImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val image = viewModel.getBitmapFromUri(context = context, uri = uri)
            viewModel.detectArtifact(image = image!!, context = context)
        }
    }

    val cameraImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            viewModel.detectArtifact(image = bitmap, context = context)
        }
    }

    LandmarksListScreenContent(
        uiState = uiState,
        hasChanged = hasChanged,
        navigateToNotifications = navigateToNotifications,
        onSearchClicked = onNavigateToSearch,
        onFilterClicked = onNavigateToFilters,
        onPlaceClicked = onNavigateToSinglePlace,
        onSaveClicked = viewModel::onSaveClicked,
        onCaptureObjectClicked = { isArtifactDetectionDialogShown = true }
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getLandmarksList()
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
            if (uiState.isSave) R.string.saved_successfully else R.string.unsaved_successfully
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

    LaunchedEffect(key1 = uiState.detectedArtifact) {
        if (uiState.detectedArtifact != null) {
            isArtifactDetectionDialogShown = false
            onNavigateToDetectedArtifact(uiState.detectedArtifact!!)
            viewModel.clearDetectionSuccess()
            Toast.makeText(context, uiState.detectedArtifact!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    if (isArtifactDetectionDialogShown) {
        ArtifactDetectionDialog(
            isDetectionLoading = uiState.isDetectionLoading,
            onDismissRequest = { isArtifactDetectionDialogShown = false },
            onGalleryClicked = {
                galleryImageLauncher.launch("image/*")
            },
            onCameraClicked = {
                if (hasCameraPermission) {
                    cameraImageLauncher.launch(null)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        )
    }
}

@Composable
fun LandmarksListScreenContent(
    uiState: LandmarksListUIState,
    hasChanged: Boolean = false,
    onSearchClicked: () -> Unit = {},
    navigateToNotifications: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onPlaceClicked: (AbstractedLandmark) -> Unit = {},
    onSaveClicked: (AbstractedLandmark) -> Unit = {},
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
            showNotifications = true,
            // TODO: Implement notifications, active tour, and active tour logic!!
//            showNotificationsBadge = true,
            showActiveTour = true,
            showCaptureObject = true,
            onSearchClicked = onSearchClicked,
            onCaptureObjectClicked = onCaptureObjectClicked,
            onNotificationsClicked = navigateToNotifications,
            onActiveTourClicked = {
                // TODO: Here!!
            }
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.landmarks.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(id = R.string.no_landmarks_found)
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.landmarks.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                PlacesHeader(
                    placesCount = uiState.displayedLandmarks.size,
                    onFilterClicked = onFilterClicked,
                    hasChanged = hasChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                PlacesSection(
                    places = uiState.displayedLandmarks,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun PlacesHeader(
    placesCount: Int,
    onFilterClicked: () -> Unit,
    hasChanged: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.landmarks_count, placesCount),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onFilterClicked()
                },
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(id = R.string.filters),
            tint = if (hasChanged) MaterialTheme.colorScheme.outlineVariant
            else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun PlacesSection(
    places: List<AbstractedLandmark>,
    onPlaceClicked: (AbstractedLandmark) -> Unit,
    onSaveClicked: (AbstractedLandmark) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = places, key = { it.id }) { place ->
            LandmarkItem(
                place = place,
                onPlaceClicked = onPlaceClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Preview
@Composable
private fun LandmarksScreenPreview() {
    EGTourGuideTheme {
        LandmarksListScreenContent(
            hasChanged = true,
            uiState = LandmarksListUIState(
                isLoading = false,
                displayedLandmarks = (0..4).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "Cairo",
                        isSaved = false,
                        rating = 6.7f,
                        ratingCount = 8388
                    )
                },
                landmarks = (0..9).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "Cairo",
                        isSaved = false,
                        rating = 6.7f,
                        ratingCount = 8388
                    )
                }
            )
        )
    }
}