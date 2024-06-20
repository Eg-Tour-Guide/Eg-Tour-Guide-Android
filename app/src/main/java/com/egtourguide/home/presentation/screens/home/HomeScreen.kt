package com.egtourguide.home.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.Event
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ScreenHeader
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToSinglePlace: (Place) -> Unit = {},
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

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedScreen = BottomBarScreens.Home
            ) { selectedScreen ->
                Log.d("```TAG```", "HomeScreen: $selectedScreen")
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
        HomeScreenContent(
            events = uiState.events,
            suggestedPlaces = uiState.suggestedPlaces,
            topRatedPlaces = uiState.topRatedPlaces,
            explorePlaces = uiState.explorePlaces,
            recentlyAddedPlaces = uiState.recentlyAddedPlaces,
            mightLikePlaces = uiState.mightLikePlaces,
            recentlyViewedPlaces = uiState.recentlyViewedPlaces,
            onSearchClicked = onNavigateToSearch,
            onNotificationClicked = onNavigateToNotification,
            onPlaceClicked = onNavigateToSinglePlace,
            onMoreClicked = onNavigateToSingleCategory,
            onEventClicked = onNavigateToEvent,
            onSaveClicked = viewModel::onSaveClicked
        )
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
            viewModel.clearSaveSuccess()
        }
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
    onSearchClicked: () -> Unit = {},
    onNotificationClicked: () -> Unit = {},
    onEventClicked: (Event) -> Unit = {},
    onPlaceClicked: (Place) -> Unit = {},
    onSaveClicked: (Place) -> Unit = {},
    onMoreClicked: (Section) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .padding(bottom = 60.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(state = scrollState)
    ) {
        ScreenHeader(
            modifier = Modifier.height(62.dp),
            showLogo = true,
            showNotifications = true,
            showSearch = true,
            onNotificationsClicked = onNotificationClicked,
            onSearchClicked = onSearchClicked
        )
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
        HomeSection(
            sectionTitle = stringResource(R.string.recently_viewed),
            sectionPlaces = recentlyViewedPlaces,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                data = "placeImages/${events[pos].images.firstOrNull()}"
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