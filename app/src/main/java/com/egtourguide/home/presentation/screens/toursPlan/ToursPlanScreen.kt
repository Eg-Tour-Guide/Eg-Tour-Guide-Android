package com.egtourguide.home.presentation.screens.toursPlan

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.egtourguide.core.presentation.components.LoadingProgress
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.components.ScreenHeader

@Preview
@Composable
private fun ToursPlanScreenPreview() {
    EGTourGuideTheme {
        ToursPlanScreenContent(
            uiState = ToursPlanScreenState()
        )
    }
}

@Composable
fun ToursPlanScreenRoot(
    viewModel: ToursPlanViewModel = hiltViewModel(),
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

    ToursPlanScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onDateChanged = viewModel::onDateChanged,
        changePickerVisibility = viewModel::changeDialogVisibility,
        changeChosenDay = viewModel::changeChosenDay,
        onPlaceClicked = navigateToLandmark
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToursPlanScreenContent(
    uiState: ToursPlanScreenState = ToursPlanScreenState(),
    onBackClicked: () -> Unit = {},
    onDateChanged: (Long) -> Unit = {},
    changePickerVisibility: () -> Unit = {},
    changeChosenDay: (Int) -> Unit = {},
    onPlaceClicked: (String) -> Unit = {}
) {
    val dateState = rememberDatePickerState()

    LaunchedEffect(key1 = dateState.selectedDateMillis) {
        onDateChanged(dateState.selectedDateMillis ?: 0)
    }

    // TODO: Check size problem!!
    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = changePickerVisibility,
            confirmButton = {
                TextButton(
                    onClick = {
                        changePickerVisibility()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { changePickerVisibility() }
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true,
                title = {
                    Text(
                        text = stringResource(id = R.string.select_date),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, start = 24.dp)
                    )
                },
                colors = DatePickerDefaults.colors(
                    dayContentColor = MaterialTheme.colorScheme.onBackground,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationContentColor = MaterialTheme.colorScheme.onBackground,
                    yearContentColor = MaterialTheme.colorScheme.onBackground,
                    disabledYearContentColor = MaterialTheme.colorScheme.outline,
                    disabledDayContentColor = MaterialTheme.colorScheme.outline,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    headlineContentColor = MaterialTheme.colorScheme.onBackground,
                    dividerColor = MaterialTheme.colorScheme.outline,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            )
        }
    }

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

        if (uiState.isLoading) {
            LoadingProgress(
                modifier = Modifier.weight(1f)
            )
        } else {
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
                        // TODO: Date passed disabled item!!
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

                items(items = uiState.days[uiState.chosenDay].orEmpty(), key = { it.id }) { place ->
                    TourPlanItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        place = place,
                        onClick = onPlaceClicked
                    )
                }
            }

            // TODO: Remove this!!
//            if (uiState.startDate != 0L && uiState.days.isNotEmpty()) {
            MainButton(
                text = stringResource(id = R.string.start_tour),
                onClick = changePickerVisibility,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            )
//            }
        }
    }
}