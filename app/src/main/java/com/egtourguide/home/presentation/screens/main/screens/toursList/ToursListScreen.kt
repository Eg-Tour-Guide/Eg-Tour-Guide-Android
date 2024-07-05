package com.egtourguide.home.presentation.screens.main.screens.toursList

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
import androidx.compose.ui.graphics.Color
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
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.components.TourItem
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.presentation.screens.main.components.ArtifactDetectionDialog
import java.util.HashMap

@Composable
fun ToursListScreen(
    viewModel: ToursListViewModel = hiltViewModel(),
    filters: HashMap<*, *>? = null,
    onNavigateToSearch: () -> Unit = {},
    navigateToNotifications: () -> Unit,
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSingleTour: (AbstractedTour) -> Unit = {},
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    viewModel.filters = filters

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

    ToursListScreenContent(
        uiState = uiState,
        onSearchClicked = onNavigateToSearch,
        onNotificationsClicked = navigateToNotifications,
        onFilterClicked = onNavigateToFilters,
        onTourClicked = onNavigateToSingleTour,
        onSaveClicked = viewModel::onSaveClicked,
        onCaptureObjectClicked = { isArtifactDetectionDialogShown = true }
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
fun ToursListScreenContent(
    uiState: ToursListUIState,
    onSearchClicked: () -> Unit = {},
    onNotificationsClicked: () -> Unit = {},
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
            showNotifications = true,
            // TODO: Implement notifications, active tour, and active tour logic!!
//            showNotificationsBadge = true,
            showActiveTour = true,
            showCaptureObject = true,
            onSearchClicked = onSearchClicked,
            onCaptureObjectClicked = onCaptureObjectClicked,
            onNotificationsClicked = onNotificationsClicked,
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
                Spacer(modifier = Modifier.height(16.dp))

                ToursHeader(
                    toursCount = uiState.tours.size,
                    onFilterClicked = onFilterClicked
                )

                Spacer(modifier = Modifier.height(16.dp))

                ToursSection(
                    tours = uiState.tours,
                    onTourClicked = onTourClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ToursHeader(
    toursCount: Int,
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
            text = "$toursCount Tours",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onFilterClicked()
            },
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "Filter Icon",
            tint = Color.Unspecified
        )
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
            TourItem(
                tour = tour,
                onTourClicked = onTourClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Preview
@Composable
private fun ToursScreenPreview() {
    EGTourGuideTheme {
        ToursListScreenContent(
            uiState = ToursListUIState(
                isLoading = false,
                tours = (0..9).map {
                    AbstractedTour(
                        id = "$it",
                        image = "contentiones",
                        title = "eros",
                        duration = 3645,
                        rating = 10.11f,
                        ratingCount = 3276,
                        isSaved = false
                    )
                }
            )
        )
    }
}