package com.egtourguide.home.presentation.screens.main.screens.landmarks_list

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.presentation.components.EmptyState
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ScreenHeader

@Composable
fun LandmarksListScreen(
    viewModel: LandmarksListViewModel = hiltViewModel(),
    filters: HashMap<*, *>? = null,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSinglePlace: (Place) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    viewModel.filters = filters

    LandmarksListScreenContent(
        uiState = uiState,
        onSearchClicked = onNavigateToSearch,
        onFilterClicked = onNavigateToFilters,
        onPlaceClicked = onNavigateToSinglePlace,
        onSaveClicked = viewModel::onSaveClicked
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getLandmarksList()
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
}

@Composable
fun LandmarksListScreenContent(
    uiState: LandmarksListUIState,
    onSearchClicked: () -> Unit,
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
            showSearch = true,
            onSearchClicked = onSearchClicked
        )
        Spacer(modifier = Modifier.height(18.dp))
        PlacesHeader(
            placesCount = uiState.landmarks.size,
            onFilterClicked = onFilterClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }
        AnimatedVisibility(
            visible = uiState.isShowEmptyState && uiState.landmarks.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(modifier = Modifier.fillMaxSize(), message = "No Landmarks Found")
        }
        AnimatedVisibility(
            visible = !uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            PlacesSection(
                places = uiState.landmarks,
                onPlaceClicked = onPlaceClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Composable
private fun PlacesHeader(
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
            text = "$placesCount Landmarks",
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
private fun LandmarksScreenPreview() {
    LandmarksListScreen()
}