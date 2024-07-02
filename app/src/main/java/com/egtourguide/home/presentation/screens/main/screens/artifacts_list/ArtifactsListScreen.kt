package com.egtourguide.home.presentation.screens.main.screens.artifacts_list

import android.widget.Toast
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.presentation.components.ArtifactItem
import com.egtourguide.home.presentation.components.EmptyState
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.ScreenHeader
import java.util.HashMap

@Composable
fun ArtifactsListScreen(
    viewModel: ArtifactsListViewModel = hiltViewModel(),
    filters: HashMap<*, *>? = null,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSingleArtifact: (AbstractedArtifact) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    viewModel.filters = filters

    ArtifactsListScreenContent(
        uiState = uiState,
        onSearchClicked = onNavigateToSearch,
        onFilterClicked = onNavigateToFilters,
        onArtifactClicked = onNavigateToSingleArtifact,
        onSaveClicked = viewModel::onSaveClicked
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getArtifactsList()
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
                "There are a problem in saving the artifact, try again later",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSaveError()
        }
    }
}

@Composable
fun ArtifactsListScreenContent(
    uiState: ArtifactsListUIState,
    onSearchClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onArtifactClicked: (AbstractedArtifact) -> Unit,
    onSaveClicked: (AbstractedArtifact) -> Unit
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
            onCaptureObjectClicked = {
                // TODO: Here!!
            },
            onNotificationsClicked = {
                // TODO: Here!!
            },
            onActiveTourClicked = {
                // TODO: And here!!
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
            visible = !uiState.isLoading && uiState.artifacts.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(id = R.string.no_artifacts_found)
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.artifacts.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                ArtifactsHeader(
                    artifactsCount = uiState.artifacts.size,
                    onFilterClicked = onFilterClicked
                )

                Spacer(modifier = Modifier.height(16.dp))

                ArtifactsSection(
                    artifacts = uiState.artifacts,
                    onArtifactClicked = onArtifactClicked,
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun ArtifactsHeader(
    artifactsCount: Int,
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
            text = "$artifactsCount Artifact",
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
private fun ArtifactsSection(
    artifacts: List<AbstractedArtifact>,
    onArtifactClicked: (AbstractedArtifact) -> Unit,
    onSaveClicked: (AbstractedArtifact) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = artifacts, key = { it.id }) { artifact ->
            ArtifactItem(
                artifact = artifact,
                onArtifactClicked = onArtifactClicked,
                onSaveClicked = onSaveClicked
            )
        }
    }
}

@Preview
@Composable
private fun ArtifactsScreenPreview() {
    EGTourGuideTheme {
        ArtifactsListScreenContent(
            uiState = ArtifactsListUIState(
                isLoading = false,
                artifacts = (0..9).map {
                    AbstractedArtifact(
                        id = "$it",
                        name = "Yolanda Koch",
                        image = "habemus",
                        isSaved = false,
                        type = "eam",
                        material = "patrioque",
                        museumName = "Jeanie Norris"
                    )
                }
            ),
            onSearchClicked = {},
            onFilterClicked = {},
            onArtifactClicked = {},
            onSaveClicked = {}
        )
    }
}