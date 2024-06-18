package com.egtourguide.home.presentation.screens.artifacts_list

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
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
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.presentation.components.ArtifactItem
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.ScreenHeader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArtifactsListScreen(
    viewModel: ArtifactsListViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToFilters: () -> Unit = {},
    onNavigateToSingleArtifact: (AbstractedArtifact) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTours: () -> Unit = {},
    onNavigateToLandmarks: () -> Unit = {},
    onNavigateToUser: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedScreen = BottomBarScreens.Artifacts
            ) { selectedScreen ->
                when (selectedScreen) {
                    BottomBarScreens.Home -> onNavigateToHome()
                    BottomBarScreens.Tours -> onNavigateToTours()
                    BottomBarScreens.Landmarks -> onNavigateToLandmarks()
                    BottomBarScreens.Artifacts -> {}
                    BottomBarScreens.User -> onNavigateToUser()
                }
            }
        }
    ) {
        ArtifactsListScreenContent(
            artifacts = uiState.artifacts,
            onSearchClicked = onNavigateToSearch,
            onNotificationClicked = onNavigateToNotification,
            onFilterClicked = onNavigateToFilters,
            onArtifactClicked = onNavigateToSingleArtifact,
            onSaveClicked = viewModel::onSaveClicked
        )
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
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
            if (uiState.isSave) "Artifact Saved Successfully" else "Artifact Unsaved Successfully"
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
    artifacts: List<AbstractedArtifact>,
    onSearchClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onArtifactClicked: (AbstractedArtifact) -> Unit,
    onSaveClicked: (AbstractedArtifact) -> Unit
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
            showNotifications = true,
            showSearch = true,
            onNotificationsClicked = onNotificationClicked,
            onSearchClicked = onSearchClicked
        )
        Spacer(modifier = Modifier.height(18.dp))
        ArtifactsHeader(
            artifactsCount = artifacts.size,
            onFilterClicked = onFilterClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        ArtifactsSection(
            artifacts = artifacts,
            onArtifactClicked = onArtifactClicked,
            onSaveClicked = onSaveClicked
        )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = artifacts) { artifact ->
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
    ArtifactsListScreen()
}