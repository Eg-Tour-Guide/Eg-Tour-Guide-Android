package com.egtourguide.expanded.presentation.screens.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.expanded.presentation.components.RatingBar
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField

@Composable
fun ReviewScreenRoot(
    viewModel: ReviewViewModel = hiltViewModel(),
    id: String,
    isLandmark: Boolean,
    onNavigateBack: () -> Unit,
    onSuccessReview: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    ReviewContent(
        onNavigateBack = onNavigateBack,
        uiState = uiState,
        onChangeReview = viewModel::changeReview,
        onSubmitClick = {
            viewModel.onSubmitClick(isLandMark = isLandmark, id = id)
        },
        onChangeRating = viewModel::changeRating
    )

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.isSuccess) {
        // TODO: Ask about message!!
        if (uiState.isSuccess) {
            Toast.makeText(
                context,
                "You Rate with ${uiState.rating} stars",
                Toast.LENGTH_SHORT
            ).show()
            onSuccessReview()
        }
    }
}

@Composable
private fun ReviewContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    uiState: ReviewState = ReviewState(),
    onChangeReview: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onChangeRating: (Int) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = onNavigateBack,
            modifier = modifier.height(52.dp)
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.rating),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier.padding(top = 16.dp)
                )

                RatingBar(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    rating = uiState.rating,
                    onRatingChanged = onChangeRating,
                    isClickable = !uiState.isLoading
                )

                Text(
                    text = stringResource(id = R.string.review),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier.padding(top = 24.dp)
                )

                MainTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    value = uiState.review,
                    onValueChanged = onChangeReview,
                    singleLine = false,
                    isEnabled = !uiState.isLoading,
                    labelText = stringResource(id = R.string.review),
                    placeholderText = stringResource(id = R.string.write_review),
                    imeAction = ImeAction.Default
                )
            }

            MainButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                text = stringResource(id = R.string.submit),
                onClick = {
                    focusManager.clearFocus()
                    onSubmitClick()
                },
                isLoading = uiState.isLoading,
                isEnabled = !uiState.isLoading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenReview() {
    EGTourGuideTheme {
        ReviewContent()
    }
}