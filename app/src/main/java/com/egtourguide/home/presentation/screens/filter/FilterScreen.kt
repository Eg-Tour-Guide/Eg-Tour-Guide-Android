package com.egtourguide.home.presentation.screens.filter

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.screens.filter.components.CustomChip
import com.egtourguide.home.presentation.screens.filter.components.CustomRangeSlider

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

                // TODO: Change these texts!!
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.days, uiState.minDuration.toInt()),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = stringResource(id = R.string.days, uiState.maxDuration.toInt()),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

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
