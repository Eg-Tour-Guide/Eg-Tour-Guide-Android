package com.egtourguide.home.presentation.screens.landmarks_artifacts

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ScreenHeader

@Composable
fun LandmarksArtifactsScreen(
    isLandmarks: Boolean = true,
    viewModel: LandmarksArtifactsViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSinglePlace: (Place) -> Unit = {}
) {
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
    LandmarksArtifactsScreenContent(
        isLandMarks = isLandmarks,
        places = places,
        onSearchClicked = onNavigateToSearch,
        onNotificationClicked = onNavigateToNotification,
        onFilterClicked = onNavigateToFilters,
        onPlaceClicked = onNavigateToSinglePlace,
        onSaveClicked = viewModel::onSaveClicked
    )
}

@Composable
fun LandmarksArtifactsScreenContent(
    isLandMarks: Boolean,
    places: List<Place>,
    onSearchClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onPlaceClicked: (Place) -> Unit,
    onSaveClicked: (Place) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(62.dp),
            showLogo = true,
            showNotifications = true,
            showSearch = true,
            onNotificationsClicked = onNotificationClicked,
            onSearchClicked = onSearchClicked
        )
        Spacer(modifier = Modifier.height(18.dp))
        PlacesHeader(
            isLandMarks = isLandMarks,
            placesCount = places.size,
            onFilterClicked = onFilterClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlacesSection(
            places = places,
            onPlaceClicked = onPlaceClicked,
            onSaveClicked = onSaveClicked
        )
    }
}

@Composable
private fun PlacesHeader(
    isLandMarks: Boolean,
    placesCount: Int,
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
            text = "$placesCount ${if (isLandMarks) " Landmarks" else " Artifacts"}",
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
private fun PlacesSection(
    places: List<Place>,
    onPlaceClicked: (Place) -> Unit,
    onSaveClicked: (Place) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = places) { place ->
            PlaceItem(
                place = place,
                onPlaceClicked = onPlaceClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Preview
@Composable
private fun LandmarksArtifactsScreenPreview() {
    LandmarksArtifactsScreen()
}