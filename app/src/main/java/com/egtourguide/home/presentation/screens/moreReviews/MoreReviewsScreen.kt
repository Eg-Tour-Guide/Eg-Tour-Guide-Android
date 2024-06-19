package com.egtourguide.home.presentation.screens.moreReviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.Review
import com.egtourguide.home.presentation.components.ReviewItem
import com.egtourguide.home.presentation.components.ReviewsHeader
import com.egtourguide.home.presentation.components.ScreenHeader

@Preview(showBackground = true)
@Composable
private fun MoreReviewsScreenPreview() {
    EGTourGuideTheme {
        MoreReviewsScreen(
            uiState = MoreReviewsScreenState(
                reviews = listOf(
                    Review(
                        id = "",
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        id = "",
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3,
                        description = getLoremString(words = 20)
                    ),
                )
            )
        )
    }
}

@Composable
fun MoreReviewsScreenRoot(
    viewModel: MoreReviewsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToReview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MoreReviewsScreen(
        uiState = uiState,
        onBackClicked = onNavigateBack,
        onReviewClicked = onNavigateToReview
    )
}

@Composable
private fun MoreReviewsScreen(
    uiState: MoreReviewsScreenState = MoreReviewsScreenState(),
    onBackClicked: () -> Unit = {},
    onReviewClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = onBackClicked,
            modifier = Modifier.height(52.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReviewsHeader(
                    reviewsAverage = uiState.reviewsAverage,
                    reviewsTotal = uiState.reviewsTotal,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(items = uiState.reviews) {
                ReviewItem(
                    review = it
                )
            }

            item {
                MainButton(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    text = stringResource(id = R.string.review),
                    onClick = onReviewClicked
                )
            }
        }
    }
}