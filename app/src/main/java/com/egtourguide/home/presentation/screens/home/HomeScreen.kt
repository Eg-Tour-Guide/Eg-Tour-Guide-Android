package com.egtourguide.home.presentation.screens.home

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.Event
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ScreenHeader
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSinglePlace: (Place) -> Unit = {},
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit = {},
    onNavigateToSingleCategory: (Section) -> Unit = {},
    onNavigateToEvent: (Event) -> Unit = {},
    onNavigateToTours: () -> Unit = {},
    onNavigateToLandmarks: () -> Unit = {},
    onNavigateToArtifacts: () -> Unit = {},
    onNavigateToUser: () -> Unit = {},
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
        contract =
        ActivityResultContracts.GetContent()
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

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedScreen = BottomBarScreens.Home
            ) { selectedScreen ->
                when (selectedScreen) {
                    BottomBarScreens.Home -> {}
                    BottomBarScreens.Tours -> onNavigateToTours()
                    BottomBarScreens.Landmarks -> onNavigateToLandmarks()
                    BottomBarScreens.Artifacts -> onNavigateToArtifacts()
                    BottomBarScreens.User -> onNavigateToUser()
                }
            }
        }
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
                showCaptureObject = true,
                onSearchClicked = onNavigateToSearch,
                onCaptureObjectClicked = {
                    isArtifactDetectionDialogShown = true
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
                    mightLikePlaces = uiState.mightLikePlaces,
                    recentlyViewedPlaces = uiState.recentlyViewedPlaces,
                    onPlaceClicked = onNavigateToSinglePlace,
                    onMoreClicked = onNavigateToSingleCategory,
                    onEventClicked = onNavigateToEvent,
                    onSaveClicked = viewModel::onSaveClicked
                )
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getHome()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
    events: List<Event> = emptyList(),
    suggestedPlaces: List<Place> = emptyList(),
    topRatedPlaces: List<Place> = emptyList(),
    explorePlaces: List<Place> = emptyList(),
    recentlyAddedPlaces: List<Place> = emptyList(),
    mightLikePlaces: List<Place> = emptyList(),
    recentlyViewedPlaces: List<Place> = emptyList(),
    onEventClicked: (Event) -> Unit = {},
    onPlaceClicked: (Place) -> Unit = {},
    onSaveClicked: (Place) -> Unit = {},
    onMoreClicked: (Section) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier.verticalScroll(state = scrollState)
    ) {
        UpcomingEventsSection(
            events = events,
            onEventClicked = onEventClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Suggested Places
        HomeSection(
            sectionTitle = stringResource(id = R.string.suggested_for_you),
            sectionPlaces = suggestedPlaces,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Top Rated Places
        HomeSection(
            sectionTitle = stringResource(id = R.string.top_rated_places),
            sectionPlaces = topRatedPlaces,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //LandMarks
        HomeSection(
            sectionTitle = stringResource(R.string.explore_egypt_s_landmarks),
            sectionPlaces = explorePlaces,
            onPlaceClicked = onPlaceClicked,
            isMore = true,
            onMoreClicked = { onMoreClicked(Section.LandMarks) },
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Recently Added
        HomeSection(
            sectionTitle = stringResource(R.string.recently_added),
            sectionPlaces = recentlyAddedPlaces,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Might Like Places
        HomeSection(
            sectionTitle = stringResource(R.string.you_might_also_like),
            sectionPlaces = mightLikePlaces,
            isMore = true,
            onPlaceClicked = onPlaceClicked,
            onMoreClicked = { onMoreClicked(Section.MightLike) },
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Recently Viewed
        if(recentlyAddedPlaces.isNotEmpty()){
            HomeSection(
                sectionTitle = stringResource(R.string.recently_viewed),
                sectionPlaces = recentlyViewedPlaces,
                onPlaceClicked = onPlaceClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UpcomingEventsSection(
    events: List<Event>,
    onEventClicked: (Event) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = { events.size })
        var currentPage by remember { mutableIntStateOf(0) }

        if (events.isNotEmpty()) {
            LaunchedEffect(key1 = currentPage) {
                pagerState.animateScrollToPage(currentPage)
                delay(5000)
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
        }

        Text(
            text = stringResource(R.string.upcoming_events),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState
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
    sectionPlaces: List<Place>,
    isMore: Boolean = false,
    onMoreClicked: () -> Unit = {},
    onPlaceClicked: (Place) -> Unit,
    onSaveClicked: (Place) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                text = sectionTitle,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (isMore) {
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onMoreClicked()
                        },
                    text = stringResource(R.string.more),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = sectionPlaces) { place ->
                PlaceItem(
                    place = place,
                    onPlaceClicked = onPlaceClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ArtifactDetectionDialog(
    isDetectionLoading: Boolean,
    onDismissRequest: () -> Unit,
    onGalleryClicked: () -> Unit,
    onCameraClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(152.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(isDetectionLoading){
                LoadingState()
            }else{
                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_camera,
                    title = "Camera",
                    onClick = { onCameraClicked() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_gallery,
                    title = "Gallery",
                    onClick = { onGalleryClicked() }
                )
            }
        }
    }
}

@Composable
private fun ArtifactDetectionDialogOption(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 16.dp, horizontal = 42.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

enum class Section {
    LandMarks,
    MightLike
}


@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    EGTourGuideTheme {
        HomeScreenContent()
    }
}