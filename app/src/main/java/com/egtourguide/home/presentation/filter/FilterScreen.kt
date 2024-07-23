package com.egtourguide.home.presentation.filter

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

@Composable
fun FilterScreen(
    viewModel: FilterScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = onNavigateBack,
            modifier = Modifier.height(52.dp)
        )

        ScreenContent(
            uiState = uiState,
            filterType = uiState.filterType,
            onCategoryChipClicked = viewModel::onCategoryChipClicked,
            onTourismTypeChipClicked = viewModel::onTourismTypeChipClicked,
            onLocationChipClicked = viewModel::onLocationChipClicked,
            onRatingChipClicked = viewModel::onRatingChipClicked,
            onSortByChipClicked = viewModel::onSortByChipClicked,
            onArtifactTypeChipClicked = viewModel::onArtifactTypeChipClicked,
            onMaterialChipClicked = viewModel::onMaterialChipClicked,
            onTourTypeChipClicked = viewModel::onTourTypeChipClicked,
            onDurationChanged = viewModel::changeDuration,
            onApplyClicked = onNavigateBack,
            onResetClicked = viewModel::onResetClicked
        )
    }
}

// TODO: Change used data according to the backend!!
@Composable
private fun ScreenContent(
    uiState: FilterScreenState,
    filterType: FilterType,
    onCategoryChipClicked: (String) -> Unit,
    onTourismTypeChipClicked: (String) -> Unit,
    onLocationChipClicked: (String) -> Unit,
    onRatingChipClicked: (String) -> Unit,
    onSortByChipClicked: (String) -> Unit,
    onArtifactTypeChipClicked: (String) -> Unit,
    onMaterialChipClicked: (String) -> Unit,
    onTourTypeChipClicked: (String) -> Unit,
    onDurationChanged: (Float, Float) -> Unit,
    onApplyClicked: () -> Unit,
    onResetClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        if (filterType == FilterType.SEARCH) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.catogry),
                    chipsTitles = categories,
                    selectedChips = listOf(uiState.selectedCategory),
                    onChipClicked = onCategoryChipClicked
                )
            }
        }

        if (filterType == FilterType.LANDMARK) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.tourism_type),
                    chipsTitles = tourismTypes,
                    selectedChips = uiState.selectedTourismTypes,
                    onChipClicked = onTourismTypeChipClicked
                )
            }
        }

        if (filterType == FilterType.ARTIFACT) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.artifact_type),
                    chipsTitles = artifactTypes,
                    selectedChips = uiState.selectedArtifactTypes,
                    onChipClicked = onArtifactTypeChipClicked
                )

                FlowFilterSection(
                    title = stringResource(id = R.string.material),
                    chipsTitles = materials,
                    selectedChips = uiState.selectedMaterials,
                    onChipClicked = onMaterialChipClicked,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }

        if (filterType == FilterType.TOUR) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.tour_type),
                    chipsTitles = tourTypes,
                    selectedChips = uiState.selectedTourTypes,
                    onChipClicked = onTourTypeChipClicked
                )

                Text(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 32.dp)
                )

                DurationFields(
                    minDuration = uiState.minDuration,
                    maxDuration = uiState.maxDuration,
                    onDurationChanged = onDurationChanged
                )

                CustomRangeSlider(
                    min = uiState.minDuration,
                    max = uiState.maxDuration,
                    onDurationChanged = onDurationChanged
                )
            }
        }

        if (filterType != FilterType.SEARCH) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.location),
                    chipsTitles = locations,
                    selectedChips = uiState.selectedLocations,
                    onChipClicked = onLocationChipClicked
                )
            }
        }

        if (filterType == FilterType.TOUR || filterType == FilterType.LANDMARK) {
            item {
                FlowFilterSection(
                    title = stringResource(id = R.string.rating),
                    isRating = true,
                    chipsTitles = ratings,
                    selectedChips = listOf(uiState.selectedRating),
                    onChipClicked = onRatingChipClicked
                )
            }
        }

        item {
            FlowFilterSection(
                title = stringResource(id = R.string.sortby),
                chipsTitles = sortWays,
                selectedChips = listOf(uiState.selectedSortBy),
                onChipClicked = onSortByChipClicked
            )
        }

        item {
            FilterFooter(
                onApplyClick = onApplyClicked,
                onResetClick = onResetClicked
            )
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
                value = if (minDuration == -1f) "" else minDuration.toInt()
                    .toString(),
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
                value = if (maxDuration == 31f) "" else maxDuration.toInt()
                    .toString(),
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
    chipsTitles: List<String>,
    selectedChips: List<String>,
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

@Preview(showBackground = true)
@Composable
private fun FilterScreenReview() {
    EGTourGuideTheme {
        FilterScreen(
            viewModel = hiltViewModel(),
            onNavigateBack = {},
//            onNavigateToResults = {}
        )
    }
}
