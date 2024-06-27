package com.egtourguide.detailsAndReviews.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.components.RatingBar
import com.egtourguide.home.presentation.components.ScreenHeader
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField


@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    source:String="",
    id: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var isPlace=false
    var isTour =false
    when (source){
        "tour"-> isTour=true
        "place"-> isPlace=true
    }
    viewModel.tourOrPlace(isTour, isPlace,id)
    ReviewContent(
        onNavigateBack = onNavigateBack,
        uiState = uiState,
        onChangeReview = viewModel::changeReview,
        onSubmitClick = viewModel::onSubmitClick,
        onChangeRating = viewModel::changeRating
    )
    LaunchedEffect(key1 = uiState.isSuccess, key2 = uiState.isError) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "You Rate with ${uiState.rating} stars", Toast.LENGTH_SHORT)
                .show()
            viewModel.clearSuccess()
            onNavigateBack()
        }
        if (uiState.isError){
            Toast.makeText(context, "Something get wrong", Toast.LENGTH_SHORT)
                .show()
            viewModel.clearError()
        }
    }
}

@Composable
private fun ReviewContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    uiState: ReviewState,
    onChangeReview: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onChangeRating: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ScreenHeader(
            showBack = true,
            onBackClicked = onNavigateBack,
            modifier = modifier
                .height(52.dp)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.rating),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .align(Alignment.Start)
            )
            RatingBar(
                modifier = modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                initialRating = uiState.rating,
                starSize = 28.dp,
                onRatingChanged = onChangeRating
            )

            Text(
                text = stringResource(id = R.string.review),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .padding(top = 16.dp)
                    .align(Alignment.Start)
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(209.dp)
                    .padding(top = 8.dp),
                value = uiState.review,
                onValueChanged = onChangeReview,
                singleLine = false,
                labelText = stringResource(id = R.string.review),
                placeholderText = stringResource(id = R.string.write_review),
                imeAction = ImeAction.Default
            )
            ReviewFooter(
                onSubmitClick = onSubmitClick,
                uiState = uiState
            )
        }

    }
}

@Composable
private fun ReviewFooter(
    modifier: Modifier = Modifier,
    onSubmitClick: () -> Unit,
    uiState: ReviewState
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            text = stringResource(id = R.string.submit),
            onClick = onSubmitClick,
            isLoading = uiState.isLoading,
            isEnabled = !uiState.isLoading
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenReview() {
    EGTourGuideTheme {
        ReviewScreen(
            id = "",
            onNavigateBack = { }
        )
    }
}