package com.egtourguide.home.presentation.myTours

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.TourItem

// TODO: Change all of this!!
@Composable
fun MyToursScreenRoot(
    viewModel: MyToursListViewModel = hiltViewModel(),
    onTourClicked: (AbstractedTour) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MyToursScreenContent(
        uiState = uiState,
        onTourClicked = onTourClicked
    )
}

@Composable
private fun MyToursScreenContent(
    uiState: MyToursListUIState,
    onTourClicked: (AbstractedTour) -> Unit,
) {
    if (uiState.isLoading) {
        LoadingState(
            modifier = Modifier
                .fillMaxWidth()
        )
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = uiState.tours) { tour ->
                TourItem(
                    tour = tour,
                    onTourClicked = onTourClicked,
                    onSaveClicked = {}
                )
            }
        }
    }
}