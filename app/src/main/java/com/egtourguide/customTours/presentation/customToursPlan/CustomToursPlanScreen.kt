package com.egtourguide.customTours.presentation.customToursPlan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.components.TourPlanItem
import com.egtourguide.expanded.domain.model.TourDetailsPlace

@Preview
@Composable
private fun CustomToursPlanScreenRootPreview() {
    EGTourGuideTheme {
        CustomToursPlanScreenContent(
            uiState = CustomToursPlanScreenState()
        )
    }
}

@Composable
fun CustomToursPlanScreenRoot(
    viewModel: CustomToursPlanViewModel = hiltViewModel(),
    tourId: String,
    onBackClicked: () -> Unit,
    navigateToLandmark: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getTourDetails(id = tourId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CustomToursPlanScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onPlaceClicked = navigateToLandmark,
        changeChosenDay = viewModel::changeChosenDay,
        onDeleteClicked = viewModel::deletePlace
    )
}

@Composable
private fun CustomToursPlanScreenContent(
    uiState: CustomToursPlanScreenState = CustomToursPlanScreenState(),
    onBackClicked: () -> Unit = {},
    changeChosenDay: (Int) -> Unit = {},
    onPlaceClicked: (String) -> Unit = {},
    onDeleteClicked: (TourDetailsPlace) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = onBackClicked,
            modifier = Modifier.height(52.dp)
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NetworkErrorScreen(modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.id.isNotEmpty() && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = uiState.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(items = uiState.days.keys.toList(), key = { it }) { day ->
                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (day == uiState.chosenDay) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.primaryContainer
                                    )
                                    .clickable { changeChosenDay(day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.day, day),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.displayMedium,
                                    color = if (day == uiState.chosenDay) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                items(items = uiState.days[uiState.chosenDay].orEmpty()) { place ->
                    TourPlanItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        place = place,
                        onClick = onPlaceClicked,
                        isCustom = true,
                        onDeleteClicked = { onDeleteClicked(place) }
                    )
                }
            }
        }
    }
}