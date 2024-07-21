package com.egtourguide.home.presentation.search

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToSearchResults: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    SearchScreenContent(
        uiState = uiState,
        onQueryChanged = viewModel::updateSearchQuery,
        onSearchClicked = {
            if (uiState.searchQuery.isNotEmpty()) onNavigateToSearchResults(uiState.searchQuery)
        },
        onClearHistoryClicked = viewModel::clearHistory,
        onSearchItemClicked = onNavigateToSearchResults
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.isCallSent) {
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
    uiState: SearchUIState = SearchUIState(),
    onQueryChanged: (String) -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onClearHistoryClicked: () -> Unit = {},
    onSearchItemClicked: (String) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 24.dp)
    ) {
        MainTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = uiState.searchQuery,
            onValueChanged = onQueryChanged,
            labelText = stringResource(id = R.string.search),
            placeholderText = stringResource(id = R.string.search_placeholder),
            leadingIcon = R.drawable.ic_search,
            imeAction = ImeAction.Search,
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() })
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.searchHistory.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            RecentSearchesSection(
                isClearHistoryLoading = uiState.isClearHistoryLoading,
                searchHistory = uiState.searchHistory,
                onClearClicked = onClearHistoryClicked,
                onSearchItemClicked = onSearchItemClicked
            )
        }
    }
}

@Composable
private fun RecentSearchesSection(
    isClearHistoryLoading: Boolean,
    searchHistory: List<String>,
    onClearClicked: () -> Unit,
    onSearchItemClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.recent_searches),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (isClearHistoryLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onClearClicked()
                        },
                    text = stringResource(id = R.string.clear),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(items = searchHistory) { item ->
                HistoryItem(
                    item = item,
                    onSearchItemClicked = onSearchItemClicked
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(
    item: String,
    onSearchItemClicked: (String) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .clickable { onSearchItemClicked(item) }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                text = item,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_search_arrow),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier.size(10.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFE6E6E6)
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    EGTourGuideTheme {
        SearchScreenContent(
            uiState = SearchUIState(
                isLoading = false,
                searchHistory = (1..5).map { "Test  $it" }
            )
        )
    }
}