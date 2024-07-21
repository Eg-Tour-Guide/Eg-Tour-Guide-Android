package com.egtourguide.home.presentation.screens.main.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.ItemType
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.AbstractedEvent
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.MediumCard
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.screens.main.components.ArtifactDetectionDialog
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    navigateToNotifications: () -> Unit,
    onNavigateToSinglePlace: (AbstractedLandmark) -> Unit,
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit,
    onNavigateToEvent: (AbstractedEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
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
            // TODO: Implement notifications & active tour logic!!
//            showNotificationsBadge = true,
            showActiveTour = true,
            showCaptureObject = true,
            onSearchClicked = onNavigateToSearch,
            onCaptureObjectClicked = {
                isArtifactDetectionDialogShown = true
            },
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
            visible = !uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HomeScreenContent(
                events = uiState.events,
                suggestedPlaces = uiState.suggestedPlaces,
                topRatedPlaces = uiState.topRatedPlaces,
                explorePlaces = uiState.explorePlaces,
                recentlyAddedPlaces = uiState.recentlyAddedPlaces,
                recentlyViewedPlaces = uiState.recentlyViewedPlaces,
                onPlaceClicked = onNavigateToSinglePlace,
                onEventClicked = onNavigateToEvent,
                onSaveClicked = viewModel::onSaveClicked
            )
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getHome()
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
private fun HomeScreenContent(
    events: List<AbstractedEvent> = emptyList(),
    suggestedPlaces: List<AbstractedLandmark> = emptyList(),
    topRatedPlaces: List<AbstractedLandmark> = emptyList(),
    explorePlaces: List<AbstractedLandmark> = emptyList(),
    recentlyAddedPlaces: List<AbstractedLandmark> = emptyList(),
    recentlyViewedPlaces: List<AbstractedLandmark> = emptyList(),
    onEventClicked: (AbstractedEvent) -> Unit = {},
    onPlaceClicked: (AbstractedLandmark) -> Unit = {},
    onSaveClicked: (AbstractedLandmark) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        if (events.isNotEmpty()) {
            item {
                UpcomingEventsSection(
                    events = events,
                    onEventClicked = onEventClicked
                )
            }
        }

        // Suggested Places
        if (suggestedPlaces.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    sectionTitle = stringResource(id = R.string.suggested_for_you),
                    sectionPlaces = suggestedPlaces,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }

        // Top Rated Places
        if (topRatedPlaces.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    sectionTitle = stringResource(id = R.string.top_rated_places),
                    sectionPlaces = topRatedPlaces,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }

        // LandMarks
        if (explorePlaces.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    sectionTitle = stringResource(R.string.explore_egypt_s_landmarks),
                    sectionPlaces = explorePlaces,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }

        // Recently Added
        if (recentlyAddedPlaces.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    sectionTitle = stringResource(R.string.recently_added),
                    sectionPlaces = recentlyAddedPlaces,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }

        // Recently Viewed
        if (recentlyViewedPlaces.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    sectionTitle = stringResource(R.string.recently_viewed),
                    sectionPlaces = recentlyViewedPlaces,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UpcomingEventsSection(
    events: List<AbstractedEvent>,
    onEventClicked: (AbstractedEvent) -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        val pagerState = rememberPagerState(pageCount = { events.size })
        var currentPage by remember { mutableIntStateOf(0) }

        LaunchedEffect(key1 = currentPage) {
            pagerState.animateScrollToPage(currentPage)
            delay(3000)
            if (currentPage + 1 > events.size - 1) {
                currentPage = 0
            } else {
                if (currentPage != pagerState.currentPage) {
                    currentPage = pagerState.currentPage
                } else {
                    currentPage += 1
                }
            }
        }

        Text(
            text = stringResource(R.string.upcoming_events),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { pos ->
            val imageScale = animateFloatAsState(
                targetValue = if (pos == pagerState.currentPage) 1f else 0.75f,
                label = ""
            )

            MainImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .graphicsLayer {
                        scaleX = imageScale.value
                        scaleY = imageScale.value
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onEventClicked(events[pos])
                    },
                data = "$LANDMARK_IMAGE_LINK_PREFIX/${events[pos].images.firstOrNull()}"
            )
        }
    }
}

@Composable
private fun HomeSection(
    sectionTitle: String,
    sectionPlaces: List<AbstractedLandmark>,
    onPlaceClicked: (AbstractedLandmark) -> Unit,
    onSaveClicked: (AbstractedLandmark) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = sectionTitle,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(items = sectionPlaces, key = { it.id }) { place ->
                MediumCard(
                    itemType = ItemType.LANDMARK,
                    image = place.image,
                    name = place.name,
                    isSaved = place.isSaved,
                    location = place.location,
                    ratingAverage = place.rating,
                    ratingCount = place.ratingCount,
                    onItemClicked = { onPlaceClicked(place) },
                    onSaveClicked = { onSaveClicked(place) },
                    modifier = Modifier.width(141.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun HomePreview() {
    EGTourGuideTheme {
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
                showNotificationsBadge = true,
                showActiveTour = true,
                showCaptureObject = true,
                onSearchClicked = {},
                onCaptureObjectClicked = {}
            )

            HomeScreenContent(
                events = listOf(
                    AbstractedEvent(
                        id = "nominavi", images = listOf(), name = "Belinda Rodgers"
                    )
                ),
                suggestedPlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "affert",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                },
                topRatedPlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "affert",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                },
                explorePlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "affert",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                },
                recentlyAddedPlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "affert",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                },
                recentlyViewedPlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "affert",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                }
            )
        }
    }
}