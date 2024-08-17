package com.egtourguide.home.presentation.main.screens.home

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.utils.ItemType
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.AbstractedEvent
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.MediumCard
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.PullToRefreshScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.main.components.ArtifactDetectionDialog
import kotlinx.coroutines.delay

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun HomePreview() {
    EGTourGuideTheme {
        HomeScreenContent(
            uiState = HomeUIState(
                isNetworkError = false,
                isLoading = false,
                events = listOf(
                    AbstractedEvent(
                        id = "0",
                        images = listOf(),
                        name = "Belinda Rodgers"
                    )
                ),
                suggestedPlaces = (0..5).map {
                    AbstractedLandmark(
                        id = "$it",
                        name = "Terrence Kane",
                        image = "pro",
                        location = "Giza",
                        category = "Sports",
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
                        location = "Giza",
                        category = "Sports",
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
                        location = "Giza",
                        category = "Sports",
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
                        location = "Giza",
                        category = "Sports",
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
                        location = "Giza",
                        category = "Sports",
                        isSaved = false,
                        rating = 2.3,
                        ratingCount = 6748
                    )
                }
            )
        )
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToSinglePlace: (AbstractedLandmark) -> Unit,
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit,
    onNavigateToEvent: (AbstractedEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var isDetectionDialogShown by remember { mutableStateOf(false) }

    PullToRefreshScreen(
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshHome
    ) {
        HomeScreenContent(
            uiState = uiState,
            onNavigateToSearch = onNavigateToSearch,
            onCaptureObjectClicked = {
                isDetectionDialogShown = true
            },
            onPlaceClicked = onNavigateToSinglePlace,
            onEventClicked = onNavigateToEvent,
            onSaveClicked = viewModel::onSaveClicked
        )
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
private fun HomeScreenContent(
    uiState: HomeUIState = HomeUIState(),
    onNavigateToSearch: () -> Unit = {},
    onCaptureObjectClicked: () -> Unit = {},
    onEventClicked: (AbstractedEvent) -> Unit = {},
    onPlaceClicked: (AbstractedLandmark) -> Unit = {},
    onSaveClicked: (AbstractedLandmark) -> Unit = {}
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
            onSearchClicked = onNavigateToSearch,
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (uiState.events.isNotEmpty()) {
                    item {
                        UpcomingEventsSection(
                            events = uiState.events,
                            onEventClicked = onEventClicked
                        )
                    }
                }

                // Suggested Places
                if (uiState.suggestedPlaces.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        HomeSection(
                            sectionTitle = stringResource(id = R.string.suggested_for_you),
                            sectionPlaces = uiState.suggestedPlaces,
                            onPlaceClicked = onPlaceClicked,
                            onSaveClicked = onSaveClicked
                        )
                    }
                }

                // Top Rated Places
                if (uiState.topRatedPlaces.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        HomeSection(
                            sectionTitle = stringResource(id = R.string.top_rated_places),
                            sectionPlaces = uiState.topRatedPlaces,
                            onPlaceClicked = onPlaceClicked,
                            onSaveClicked = onSaveClicked
                        )
                    }
                }

                // LandMarks
                if (uiState.explorePlaces.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        HomeSection(
                            sectionTitle = stringResource(R.string.explore_egypt_s_landmarks),
                            sectionPlaces = uiState.explorePlaces,
                            onPlaceClicked = onPlaceClicked,
                            onSaveClicked = onSaveClicked
                        )
                    }
                }

                // Recently Added
                if (uiState.recentlyAddedPlaces.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        HomeSection(
                            sectionTitle = stringResource(R.string.recently_added),
                            sectionPlaces = uiState.recentlyAddedPlaces,
                            onPlaceClicked = onPlaceClicked,
                            onSaveClicked = onSaveClicked
                        )
                    }
                }

                // Recently Viewed
                if (uiState.recentlyViewedPlaces.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        HomeSection(
                            sectionTitle = stringResource(R.string.recently_viewed),
                            sectionPlaces = uiState.recentlyViewedPlaces,
                            onPlaceClicked = onPlaceClicked,
                            onSaveClicked = onSaveClicked
                        )
                    }
                }
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
                data = "$LANDMARK_IMAGE_LINK_PREFIX/${events[pos].images.firstOrNull()}",
                placeholderImage = R.drawable.ic_event,
                errorImage = R.drawable.ic_event
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