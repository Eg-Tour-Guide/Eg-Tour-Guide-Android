package com.egtourguide.home.presentation.screens.home

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ScreenHeader
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToSinglePlace: (Place) -> Unit = {},
    onNavigateToSingleCategory: (Section) -> Unit = {},
    onNavigateToEvent: (String) -> Unit = {}
) {
    HomeScreenContent(
        onSearchClicked = onNavigateToSearch,
        onNotificationClicked = onNavigateToNotification,
        onPlaceClicked = onNavigateToSinglePlace,
        onMoreClicked = onNavigateToSingleCategory,
        onEventClicked = onNavigateToEvent,
        onSaveClicked = viewModel::onSaveClicked
    )
}

@Composable
private fun HomeScreenContent(
    onSearchClicked: () -> Unit = {},
    onNotificationClicked: () -> Unit = {},
    onEventClicked: (String) -> Unit = {},
    onPlaceClicked: (Place) -> Unit = {},
    onSaveClicked: (Place) -> Unit = {},
    onMoreClicked: (Section) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    //TODO Remove temp data
    val eventsList = listOf(
        "https://buffer.com/library/content/images/2023/10/free-images.jpg",
        "https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg",
        "https://www.shutterstock.com/image-photo/damietta-city-north-egypt-600nw-693231082.jpg",
        "https://media.gettyimages.com/id/599332966/photo/fishing-boats-at-damietta.jpg?s=612x612&w=gi&k=20&c=2bcrZaQYQjQt9wH_4iU1IKdprAY81f-FRS9hn-lz5L4="
    )
    val places = listOf(
        Place(
            id = "",
            name = "Pyramids",
            image = "https://www.worldhistory.org/uploads/images/5687.jpg",
            location = "Giza",
            isSaved = false,
            rating = 4.576f,
            ratingCount = 72
        ),
        Place(
            id = "",
            name = "Pyramids",
            image = "https://www.worldhistory.org/uploads/images/5687.jpg",
            location = "Giza",
            isSaved = false,
            rating = 4.576f,
            ratingCount = 72
        ),
        Place(
            id = "",
            name = "Pyramids",
            image = "https://www.worldhistory.org/uploads/images/5687.jpg",
            location = "Giza",
            isSaved = false,
            rating = 4.576f,
            ratingCount = 72
        ),
        Place(
            id = "",
            name = "Pyramids",
            image = "https://www.worldhistory.org/uploads/images/5687.jpg",
            location = "Giza",
            isSaved = false,
            rating = 4.576f,
            ratingCount = 72
        )
    )

    Column(
        Modifier
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
            events = eventsList,
            onEventClicked = onEventClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Suggested Places
        HomeSection(
            sectionTitle = stringResource(id = R.string.suggested_for_you),
            sectionPlaces = places,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Top Rated Places
        HomeSection(
            sectionTitle = stringResource(id = R.string.top_rated_places),
            sectionPlaces = places,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //LandMarks
        HomeSection(
            sectionTitle = stringResource(R.string.explore_egypt_s_landmarks),
            sectionPlaces = places,
            onPlaceClicked = onPlaceClicked,
            isMore = true,
            onMoreClicked = { onMoreClicked(Section.LandMarks) },
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Recently Added
        HomeSection(
            sectionTitle = stringResource(R.string.recently_added),
            sectionPlaces = places,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Might Like Places
        HomeSection(
            sectionTitle = stringResource(R.string.you_might_also_like),
            sectionPlaces = places,
            isMore = true,
            onPlaceClicked = onPlaceClicked,
            onMoreClicked = { onMoreClicked(Section.MightLike) },
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Recently Viewed
        HomeSection(
            sectionTitle = stringResource(R.string.recently_viewed),
            sectionPlaces = places,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


//TODO Replace string with actual event data class
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UpcomingEventsSection(
    events: List<String>,
    onEventClicked: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = { events.size })
        var currentPage by remember { mutableIntStateOf(0) }
        var contentPaddingValues by remember { mutableStateOf(PaddingValues(end = 30.dp)) }


        LaunchedEffect(key1 = currentPage) {
            pagerState.animateScrollToPage(currentPage)
            delay(5000)
            if (currentPage + 1 > events.size - 1) {
                currentPage = 0
            } else {
                if(currentPage != pagerState.currentPage){
                    currentPage = pagerState.currentPage+1
                }else{
                    currentPage += 1
                }
            }
        }

        Log.d("```TAG```", "cp: $currentPage")
        contentPaddingValues = when (currentPage) {
            0 -> PaddingValues(end = 30.dp)
            events.size - 1 -> PaddingValues(start = 30.dp)
            else -> PaddingValues(start = 30.dp, end = 30.dp)
        }

        Text(
            text = stringResource(R.string.upcoming_events),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            pageSpacing = (-20).dp,
            /*contentPadding = PaddingValues(
                start = if (currentPage != 0) 30.dp else 0.dp,
                end = if (currentPage != events.size - 1) 30.dp else 0.dp
            )*/
            contentPadding = contentPaddingValues,
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
                data = events[pos],
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
            horizontalArrangement = Arrangement.SpaceBetween
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