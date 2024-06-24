package com.egtourguide.home.presentation.screens.filter

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.components.ChipFilter
import com.egtourguide.home.presentation.components.RangeSliderWithBubble
import com.egtourguide.home.presentation.components.ScreenHeader


@Composable
fun FilterScreen(
    viewModel: FilterScreenViewModel = hiltViewModel(),
    source: String,
    query: String = "",
    onNavigateToResults: (HashMap<String,List<String>>) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isSearch = true
    var isArtifacts = false
    var isLandmarks = false
    var isTours = false

    when (source) {
        "artifact" -> isArtifacts = true
        "tour" -> isTours = true
        "landmark" -> isLandmarks = true
        "saved" -> isLandmarks = true
        "search" -> isSearch = true
        "my_tours" -> isTours = true
    }
    if (isSearch) {
        viewModel.saveQuery(query)
    }
    if (isTours) {
        viewModel.tourScreen()
    }
    if (isLandmarks) {
        viewModel.landmarkScreen()
    }
    if (isArtifacts) {
        viewModel.artifactScreen()
    }

    LaunchedEffect(key1 = uiState.isSuccess) {
        viewModel.clearSuccess()
        onNavigateToResults(uiState.selectedMap!!)

    }


    Column {
        ScreenHeader(
            showBack = true,
            onBackClicked = onNavigateBack
        )

        ScreenContent(
            uiState = uiState,
            reset = uiState.reset,
            isSearch = isSearch,
            isTours = uiState.isTours,
            isLandmarks = uiState.isLandmarks,
            isArtifacts = uiState.isArtifacts,
            durationUpdate = viewModel::changeDuration,
            addToSelectedCategoryList = viewModel::addSelectedCategoryFilter,
            addToSelectedLocationList = viewModel::addSelectedLocationFilter,
            removeFromSelectedLocationList = viewModel::removeSelectedLocationFilter,
            addToSelectedTourTypeList = viewModel::addSelectedTourTypeFilter,
            removeFromSelectedTourTypeList = viewModel::removeSelectedTourTypeFilter,
            addToSelectedMaterialList = viewModel::addSelectedMaterialFilter,
            removeFromSelectedMaterialList = viewModel::removeSelectedMaterialFilter,
            addToSelectedArtifactTypeList = viewModel::addSelectedArtifactTypeFilter,
            removeFromSelectedArtifactList = viewModel::removeSelectedArtifactTypeFilter,
            addToSelectedRatingList = viewModel::addSelectedRatingFilter,
            removeFromSelectedRatingList = viewModel::removeSelectedRatingFilter,
            addToSelectedSortByList = viewModel::addSelectedSortByFilter,
            removeFromSelectedSortByList = viewModel::removeSelectedSortByFilter,
            addToSelectedTourismTypeList = viewModel::addSelectedTourismTypeFilter,
            removeFromSelectedTourismTypeList = viewModel::removeSelectedTourismTypeFilter,
            onApplyClick = viewModel::onApplyClick,
            onResetClick = viewModel::onResetClick
        )
    }
}


@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: FilterScreenState,
    reset: Boolean,
    isSearch: Boolean,
    isLandmarks: Boolean,
    isTours: Boolean,
    isArtifacts: Boolean,
    durationUpdate: (Int, Int) -> Unit,
    addToSelectedCategoryList: (String) -> Unit,
    addToSelectedLocationList: (String) -> Unit,
    removeFromSelectedLocationList: (String) -> Unit,
    addToSelectedTourTypeList: (String) -> Unit,
    removeFromSelectedTourTypeList: (String) -> Unit,
    addToSelectedMaterialList: (String) -> Unit,
    removeFromSelectedMaterialList: (String) -> Unit,
    addToSelectedArtifactTypeList: (String) -> Unit,
    removeFromSelectedArtifactList: (String) -> Unit,
    addToSelectedRatingList: (String) -> Unit,
    removeFromSelectedRatingList: (String) -> Unit,
    addToSelectedSortByList: (String) -> Unit,
    removeFromSelectedSortByList: (String) -> Unit,
    addToSelectedTourismTypeList: (String) -> Unit,
    removeFromSelectedTourismTypeList: (String) -> Unit,
    onApplyClick: () -> Unit,
    onResetClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        if (isSearch) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.catogry),
                    list = uiState.categoryFilters!!,
                    reset = reset,
                    addToSelectedList = addToSelectedCategoryList,
                    removeFromSelectedList = {}
                )
            }
        }
        if (isLandmarks) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.tourism_type),
                    list = uiState.tourismTypeFilters!!,
                    addToSelectedList = addToSelectedTourismTypeList,
                    removeFromSelectedList = removeFromSelectedTourismTypeList
                )
            }
        }
        if (isArtifacts) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.material),
                    list = uiState.materialList!!,
                    addToSelectedList = addToSelectedMaterialList,
                    removeFromSelectedList = removeFromSelectedMaterialList
                )

                FlowFilterSection(
                    text = stringResource(id = R.string.artifact_type),
                    list = uiState.artifactTypeList!!,
                    addToSelectedList = addToSelectedArtifactTypeList,
                    removeFromSelectedList = removeFromSelectedArtifactList
                )
            }
        }
        if (isTours) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.tour_type),
                    list = uiState.tourTypeList!!,
                    addToSelectedList = addToSelectedTourTypeList,
                    removeFromSelectedList = removeFromSelectedTourTypeList
                )
                Text(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .padding(top = 16.dp)
                )
//                DurationSlider(onValueChange = durationUpdate)
                RangeSliderWithBubble(durationUpdate)
            }
        }
        item {
            FlowFilterSection(
                text = stringResource(id = R.string.location),
                list = uiState.locationFilters!!,
                addToSelectedList = addToSelectedLocationList,
                removeFromSelectedList = removeFromSelectedLocationList
            )

            FlowFilterSection(
                text = stringResource(id = R.string.rating),
                list = uiState.ratingFilters!!,
                isRating = true,
                addToSelectedList = addToSelectedRatingList,
                removeFromSelectedList = removeFromSelectedRatingList
            )

            FlowFilterSection(
                text = stringResource(id = R.string.sortby),
                list = uiState.sortList!!,
                addToSelectedList = addToSelectedSortByList,
                removeFromSelectedList = removeFromSelectedSortByList
            )
        }
        item {
            FilterFooter(
                onResetClick = onResetClick,
                onApplyClick = onApplyClick
            )
        }
    }
}

//TODO(Handle isCategory)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowFilterSection(
    modifier: Modifier = Modifier,
    text: String,
    list: List<String>,
    reset: Boolean = false,
    isCategory: Boolean = false,
    isRating: Boolean = false,
    addToSelectedList: (String) -> Unit,
    removeFromSelectedList: (String) -> Unit
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp)
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        list.forEach { item ->
            ChipFilter(
                text = item,
                isRating = isRating,
                selected = false,
                reset = reset,
                addSelectedFilter = addToSelectedList,
                removeSelectedFilter = removeFromSelectedList,
                modifier = modifier
                    .padding(top = 16.dp)
            )
        }
    }
}


@Composable
private fun FilterFooter(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 24.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(40.dp)
    ) {
        MainButton(
            text = stringResource(id = R.string.reset),
            onClick = onResetClick,
            isWightBtn = true,
            isLoading = isLoading,
            isEnabled = !isLoading,
            modifier = modifier
                .weight(0.5f)

        )

        MainButton(
            text = stringResource(id = R.string.apply),
            onClick = onApplyClick,
            isLoading = isLoading,
            isEnabled = !isLoading,
            modifier = modifier
                .weight(0.5f)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun FilterScreenReview() {
    EGTourGuideTheme {
        FilterScreen(
            source = "",
            onNavigateBack = {},
            onNavigateToResults = {}
        )
    }

}
