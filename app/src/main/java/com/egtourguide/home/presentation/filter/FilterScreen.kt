package com.egtourguide.home.presentation.filter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.CustomTextField
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.filter.components.CustomChip
import com.egtourguide.home.presentation.filter.components.CustomRangeSlider

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun FilterScreenReview() {
    EGTourGuideTheme {
        FilterScreen(
            viewModel = hiltViewModel(),
            onNavigateBack = {}
        )
    }
}

@Composable
fun FilterScreen(
    viewModel: FilterScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.resetSelected()
        onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = {
                viewModel.resetSelected()
                onNavigateBack()
            },
            modifier = Modifier.height(52.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            if (uiState.filterType == FilterType.SEARCH || uiState.filterType == FilterType.SAVED) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.category),
                        chipsTitles = categories,
                        selectedChips = listOf(uiState.selectedCategory),
                        onChipClicked = viewModel::onCategoryChipClicked
                    )
                }
            }

            if (uiState.tourismTypes.isNotEmpty()) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.tourism_type),
                        chipsTitles = uiState.tourismTypes,
                        selectedChips = uiState.selectedTourismTypes,
                        onChipClicked = viewModel::onTourismTypeChipClicked
                    )
                }
            }

            if (uiState.artifactTypes.isNotEmpty()) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.artifact_type),
                        chipsTitles = uiState.artifactTypes,
                        selectedChips = uiState.selectedArtifactTypes,
                        onChipClicked = viewModel::onArtifactTypeChipClicked
                    )
                }
            }

            if (uiState.materials.isNotEmpty()) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.material),
                        chipsTitles = uiState.materials,
                        selectedChips = uiState.selectedMaterials,
                        onChipClicked = viewModel::onMaterialChipClicked,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }

            if (uiState.tourTypes.isNotEmpty()) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.tour_type),
                        chipsTitles = uiState.tourTypes,
                        selectedChips = uiState.selectedTourTypes,
                        onChipClicked = viewModel::onTourTypeChipClicked
                    )
                }
            }

            if (uiState.filterType == FilterType.TOUR || uiState.filterType == FilterType.MY_TOURS) {
                item {
                    Text(
                        text = stringResource(id = R.string.duration),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    DurationFields(
                        minDuration = uiState.selectedMinDuration,
                        maxDuration = uiState.selectedMaxDuration,
                        onDurationChanged = viewModel::changeDuration
                    )

                    CustomRangeSlider(
                        min = uiState.selectedMinDuration,
                        max = uiState.selectedMaxDuration,
                        onDurationChanged = viewModel::changeDuration
                    )
                }
            }

            if (uiState.locations.isNotEmpty()) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.location),
                        chipsTitles = uiState.locations,
                        selectedChips = uiState.selectedLocations,
                        onChipClicked = viewModel::onLocationChipClicked
                    )
                }
            }

            if (uiState.filterType == FilterType.TOUR || uiState.filterType == FilterType.LANDMARK) {
                item {
                    FlowFilterSection(
                        title = stringResource(id = R.string.rating),
                        isRating = true,
                        ratings = ratings,
                        selectedRate = uiState.selectedRating,
                        onChipClicked = { viewModel.onRatingChipClicked(it.toInt()) }
                    )

                    FlowFilterSection(
                        title = stringResource(id = R.string.sort_by),
                        sortWays = sortWays,
                        selectedSortWay = uiState.selectedSortBy,
                        onChipClicked = { viewModel.onSortByChipClicked(it.toInt()) },
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }

            item {
                FilterFooter(
                    onApplyClick = {
                        viewModel.onApplyClicked()
                        onNavigateBack()
                    },
                    onResetClick = viewModel::onResetClicked
                )
            }
        }
    }
}

@Composable
private fun DurationFields(
    minDuration: Float,
    maxDuration: Float,
    onDurationChanged: (Float, Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            var isFocused by remember { mutableStateOf(false) }

            Text(
                text = stringResource(id = R.string.min),
                style = MaterialTheme.typography.titleMedium,
                color = if (isFocused) MaterialTheme.colorScheme.outlineVariant
                else MaterialTheme.colorScheme.onBackground
            )

            CustomTextField(
                value = if (minDuration == -1f) "" else minDuration.toInt().toString(),
                onValueChanged = { newValue ->
                    if (newValue.isEmpty()) {
                        onDurationChanged(-1f, maxDuration)
                    } else {
                        val intValue = newValue.toIntOrNull()
                        if (intValue != null && intValue in 0..30) {
                            onDurationChanged(newValue.toFloat(), maxDuration)
                        }
                    }
                },
                isFocused = isFocused,
                onFocusChanged = { isFocused = it },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(33.dp)
                    .width(50.dp)
            )
        }

        Column {
            var isFocused by remember { mutableStateOf(false) }

            Text(
                text = stringResource(id = R.string.max),
                style = MaterialTheme.typography.titleMedium,
                color = if (isFocused) MaterialTheme.colorScheme.outlineVariant
                else MaterialTheme.colorScheme.onBackground
            )

            CustomTextField(
                value = if (maxDuration == 31f) "" else maxDuration.toInt().toString(),
                onValueChanged = { newValue ->
                    if (newValue.isEmpty()) {
                        onDurationChanged(minDuration, 31f)
                    } else {
                        val intValue = newValue.toIntOrNull()
                        if (intValue != null && intValue in 0..30) {
                            onDurationChanged(minDuration, intValue.toFloat())
                        }
                    }
                },
                isFocused = isFocused,
                onFocusChanged = { isFocused = it },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(33.dp)
                    .width(50.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowFilterSection(
    modifier: Modifier = Modifier,
    title: String,
    chipsTitles: List<String> = emptyList(),
    ratings: List<Rate> = emptyList(),
    sortWays: List<SortWay> = emptyList(),
    selectedChips: List<String> = emptyList(),
    selectedRate: Int = 0,
    selectedSortWay: Int = 0,
    isRating: Boolean = false,
    onChipClicked: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayMedium
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            chipsTitles.forEach { item ->
                CustomChip(
                    title = item,
                    isRating = isRating,
                    isSelected = (item in selectedChips),
                    onClicked = onChipClicked
                )
            }

            ratings.forEach { rate ->
                CustomChip(
                    title = rate.title,
                    isRating = isRating,
                    isSelected = (rate.rating == selectedRate),
                    onClicked = { onChipClicked(rate.rating.toString()) }
                )
            }

            sortWays.forEach { item ->
                CustomChip(
                    title = item.title,
                    isRating = isRating,
                    isSelected = (item.number == selectedSortWay),
                    onClicked = { onChipClicked(item.number.toString()) }
                )
            }
        }
    }
}

@Composable
private fun FilterFooter(
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        MainButton(
            text = stringResource(id = R.string.reset),
            onClick = onResetClick,
            isWightBtn = true,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
        )

        MainButton(
            text = stringResource(id = R.string.apply),
            onClick = onApplyClick,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
        )
    }
}