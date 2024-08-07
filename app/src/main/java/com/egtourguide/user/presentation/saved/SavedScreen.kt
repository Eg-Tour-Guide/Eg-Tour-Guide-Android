package com.egtourguide.user.presentation.saved

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.egtourguide.core.presentation.components.DataScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.user.domain.model.AbstractSavedItem
import com.egtourguide.core.presentation.components.EmptyState
import com.egtourguide.core.presentation.components.LargeCard
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.ScreenHeader

@Preview
@Composable
private fun SavedScreenPreview() {
    EGTourGuideTheme {
        SavedScreenContent(
            uiState = SavedScreenUIState(
                isLoading = false,
                savedList = (1..5).map {
                    AbstractSavedItem(
                        id = "$it",
                        image = "",
                        name = "Test",
                        itemType = ItemType.LANDMARK
                    )
                }
            )
        )
    }
}

// TODO: Filters logic!!
@Composable
fun SavedScreen(
    viewModel: SavedViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSingleItem: (AbstractSavedItem) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    SavedScreenContent(
        uiState = uiState,
        onFilterClicked = onNavigateToFilters,
        onSaveClicked = viewModel::onSaveClicked,
        onItemClicked = onNavigateToSingleItem,
        onBackClicked = onNavigateBack
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.isCallSent) {
                viewModel.getSavedItems()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // TODO: Change save logic!!
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
fun SavedScreenContent(
    uiState: SavedScreenUIState = SavedScreenUIState(),
    onFilterClicked: () -> Unit = {},
    onItemClicked: (AbstractSavedItem) -> Unit = {},
    onSaveClicked: (AbstractSavedItem) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(52.dp),
            showBack = true,
            onBackClicked = onBackClicked
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
            NetworkErrorScreen(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.savedList.isEmpty() && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                DataScreenHeader(
                    title = stringResource(id = R.string.saved_count, uiState.savedList.size),
                    onFilterClicked = onFilterClicked,
                    hasChanged = false, // TODO: Change this!!
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ItemsSection(
                    items = uiState.savedList,
                    onItemClicked = onItemClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ItemsSection(
    items: List<AbstractSavedItem>,
    onItemClicked: (AbstractSavedItem) -> Unit,
    onSaveClicked: (AbstractSavedItem) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = items, key = { it.id }) { item ->
            LargeCard(
                itemType = item.itemType,
                image = item.image,
                name = item.name,
                isSaved = item.isSaved,
                duration = item.duration,
                location = item.location,
                ratingAverage = item.ratingAverage,
                ratingCount = item.ratingCount,
                artifactType = item.artifactType,
                onItemClicked = { onItemClicked(item) },
                onSaveClicked = { onSaveClicked(item) }
            )
        }
    }
}