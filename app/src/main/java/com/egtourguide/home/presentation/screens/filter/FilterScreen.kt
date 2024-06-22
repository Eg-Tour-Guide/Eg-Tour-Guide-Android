package com.egtourguide.home.presentation.screens.filter

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.egtourguide.home.presentation.components.DurationSlider
import com.egtourguide.home.presentation.components.ScreenHeader


@Composable
fun FilterScreen(
    viewModel: FilterScreenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = listOf("Tours", "Landmarks", "Artifacts")
    Column {
        ScreenHeader(
            showBack = true,
            onBackClicked = onNavigateBack
        )

        ScreenContent(
            uiState = uiState,
            durationUpdate = viewModel::changeDuration,
            addToSelectedList = viewModel::addSelectedFilters,
            removeFromSelectedList = viewModel::removeSelectedFilter,
            onNavigateBack = onNavigateBack
        )
    }

}


@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: FilterScreenState,
    isSearch: Boolean = true,
    isTours: Boolean = true,
    isLandmarks: Boolean = true,
    isArtifacts: Boolean = true,
    durationUpdate: (Int) -> Unit,
    addToSelectedList: (String) -> Unit,
    removeFromSelectedList: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp)
    ) {
        if (isSearch) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.catogry),
                    list = uiState.categoryFilters!!,
                    addToSelectedList = addToSelectedList,
                    removeFromSelectedList = removeFromSelectedList
                )
            }

        }
        if (isLandmarks) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.tourism_type),
                    list = uiState.tourismTypeFilters!!,
                    addToSelectedList = addToSelectedList,
                    removeFromSelectedList = removeFromSelectedList
                )
            }

        }
        if (!isArtifacts) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.material),
                    list = uiState.materialList!!,
                    addToSelectedList = addToSelectedList,
                    removeFromSelectedList = removeFromSelectedList
                )

                FlowFilterSection(
                    text = stringResource(id = R.string.artifact_type),
                    list = uiState.artifactTypeList!!,
                    addToSelectedList = addToSelectedList,
                    removeFromSelectedList = removeFromSelectedList
                )
            }
        }
        if (isTours) {
            item {
                FlowFilterSection(
                    text = stringResource(id = R.string.material),
                    list = uiState.materialList!!,
                    addToSelectedList = addToSelectedList,
                    removeFromSelectedList = removeFromSelectedList
                )
                Text(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .padding(top = 16.dp)
                )
                DurationSlider(onValueChange = durationUpdate)
            }
        }
        item {
            FlowFilterSection(
                text = stringResource(id = R.string.location),
                list = uiState.locationFilters!!,
                addToSelectedList = addToSelectedList,
                removeFromSelectedList = removeFromSelectedList
            )

            FlowFilterSection(
                text = stringResource(id = R.string.rating),
                list = uiState.ratingFilters!!,
                isRating = true,
                addToSelectedList = addToSelectedList,
                removeFromSelectedList = removeFromSelectedList
            )

            FlowFilterSection(
                text = stringResource(id = R.string.sortby),
                list = uiState.sortList!!,
                addToSelectedList = addToSelectedList,
                removeFromSelectedList = removeFromSelectedList
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
    list: List<Filter>,
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
            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
    )

    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        list.forEach { item ->
            ChipFilter(
                text = item.label,
                isRating = isRating,
                selected = item.isSelected,
                addSelectedFilter = addToSelectedList,
                removeSelectedFilter = removeFromSelectedList,
                modifier = modifier
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FilterScreenReview() {
    EGTourGuideTheme {
        FilterScreen(
            onNavigateBack = {}
        )
    }

}
