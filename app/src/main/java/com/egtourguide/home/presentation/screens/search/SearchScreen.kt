package com.egtourguide.home.presentation.screens.search

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    bottomBarSelectedScreen: BottomBarScreens,
    onNavigateToHome: () -> Unit,
    onNavigateToTours: () -> Unit,
    onNavigateToLandmarks: () -> Unit,
    onNavigateToArtifacts: () -> Unit,
    onNavigateToUser: () -> Unit,
    onNavigateToSearchResults: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(selectedScreen = bottomBarSelectedScreen) { clickedScreen ->
                when (clickedScreen) {
                    BottomBarScreens.Home -> onNavigateToHome()
                    BottomBarScreens.Tours -> onNavigateToTours()
                    BottomBarScreens.Landmarks -> onNavigateToLandmarks()
                    BottomBarScreens.Artifacts -> onNavigateToArtifacts()
                    BottomBarScreens.User -> onNavigateToUser()
                }
            }
        }
    ) {
        SearchScreenContent(
            uiState = uiState,
            onQueryChanged = viewModel::updateSearchQuery,
            onSearchClicked = viewModel::getSearchSuggestions,
            onClearHistoryClicked = viewModel::clearHistory,
            onSearchItemClicked = onNavigateToSearchResults
        )
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getSearchHistory()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@Composable
private fun SearchScreenContent(
    uiState: SearchUIState,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearHistoryClicked: () -> Unit,
    onSearchItemClicked: (String) -> Unit,
) {
    Column(
        Modifier
            .padding(bottom = 60.dp, top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize()
    ) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChanged = onQueryChanged,
            onSearchClicked = onSearchClicked
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = uiState.searchQuery.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            RecentSearchesSection(
                searchHistory = uiState.searchHistory,
                onClearClicked = onClearHistoryClicked,
                onSearchItemClicked = onSearchItemClicked
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChanged = onQueryChanged,
        placeholderText = stringResource(R.string.search),
        leadingIcon = R.drawable.ic_search,
        imeAction = ImeAction.Search,
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() })
    )
}

@Composable
private fun RecentSearchesSection(
    searchHistory: List<String>,
    onClearClicked: () -> Unit,
    onSearchItemClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recent_searches),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClearClicked()
                    },
                text = stringResource(R.string.clear),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(items = searchHistory) { item ->
                SearchItem(
                    item = item,
                    onSearchItemClicked = onSearchItemClicked
                )
            }
        }
    }
}

@Composable
private fun SearchItem(
    item: String,
    onSearchItemClicked: (String) -> Unit,
) {
    Column(
        Modifier
            .padding(top = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSearchItemClicked(item)
            }

    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = item,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_search_arrow),
                contentDescription = "Arrow Icon"
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {

}