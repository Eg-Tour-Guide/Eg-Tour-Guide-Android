package com.egtourguide.home.presentation.screens.expanded

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.Review
import com.egtourguide.home.presentation.components.ScreenHeader

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun ExpandedScreenPreview() {
    EGTourGuideTheme {
        ExpandedScreen(
            uiState = ExpandedScreenState(
                images = listOf("", "", "", ""),
                title = "Pyramids",
                location = "Giza",
                reviewsAverage = 4.5,
                reviews = listOf(
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 2.3,
                        description = LoremIpsum(words = 20).values.iterator().asSequence()
                            .joinToString(" ")
                    )
                ),
                tourismTypes = listOf("Adventure", "Historical"),
                description = LoremIpsum(words = 50).values.iterator().asSequence()
                    .joinToString(" ")
            )
        )
    }
}

@Composable
fun ExpandedScreenRoot(
    viewModel: ExpandedViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSeeMoreClicked: () -> Unit,
    onReviewClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ExpandedScreen(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onSaveClicked = viewModel::changeSavedState,
        onSeeMoreClicked = onSeeMoreClicked,
        onReviewClicked = onReviewClicked
    )
}

@Composable
private fun ExpandedScreen(
    uiState: ExpandedScreenState,
    onBackClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onSeeMoreClicked: () -> Unit = {},
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            ImagesSection(
                images = uiState.images,
                title = uiState.title
            )

            TitleSection(
                title = uiState.title,
                isSaved = uiState.isSaved,
                onSaveClicked = onSaveClicked,
                location = uiState.location,
                reviewsAverage = uiState.reviewsAverage,
                reviewsTotal = uiState.reviews.size,
                tourismTypes = uiState.tourismTypes,
                modifier = Modifier.padding(top = 16.dp)
            )

            DescriptionSection(
                description = uiState.description,
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
            )

            LocationSection(
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
            )

            ReviewsSection(
                reviewsAverage = uiState.reviewsAverage,
                reviews = uiState.reviews,
                onSeeMoreClicked = onSeeMoreClicked,
                onReviewClicked = onReviewClicked,
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
            )

            IncludedArtifactsSection(
                modifier = Modifier.padding(top = 24.dp)
            )

            RelatedPlacesSection(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ImagesSection(
    images: List<String>,
    title: String
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState {
        images.size
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TODO: Change The default images!!
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(images[it])
                    .crossfade(true)
                    .placeholder(R.drawable.welcome)
                    .error(R.drawable.welcome)
                    .build(),
                contentDescription = stringResource(id = R.string.image_num, title, it + 1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(246.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                if (pagerState.currentPage == iteration) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleSection(
    title: String,
    isSaved: Boolean,
    onSaveClicked: () -> Unit,
    location: String,
    reviewsAverage: Double,
    reviewsTotal: Int,
    tourismTypes: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = stringResource(R.string.location),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = location,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rating_star),
                        contentDescription = null,
                        tint = Color(0xFFFF8D18),
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = stringResource(
                            id = R.string.reviews_average_total,
                            reviewsAverage,
                            reviewsTotal
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_landmarks),
                        contentDescription = stringResource(R.string.tourism_types),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = tourismTypes.joinToString(", "),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }

            IconButton(
                onClick = onSaveClicked
            ) {
                Icon(
                    painter = painterResource(id = if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                    contentDescription = stringResource(id = if (isSaved) R.string.un_save else R.string.save),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.description),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        SelectionContainer(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun LocationSection(
//    lat lng,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.location),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        // TODO: Replace with a static map!!
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(125.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
private fun ReviewsSection(
    reviewsAverage: Double,
    reviews: List<Review>,
    onSeeMoreClicked: () -> Unit,
    onReviewClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.reviews),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_rating_star),
                contentDescription = null,
                tint = Color(0xFFFF8D18),
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(
                    id = R.string.reviews_average_total,
                    reviewsAverage,
                    reviews.size
                ),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Load user image!!
            Image(
                painter = painterResource(id = R.drawable.ic_profile_pic),
                contentDescription = stringResource(id = R.string.profile_picture),
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = reviews[0].authorName,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // TODO: Replace with rating bar!!
        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(5) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rating_star),
                    contentDescription = "${reviews[0].rating} Rating",
                    tint = if (it > reviews[0].rating.toInt()) MaterialTheme.colorScheme.primaryContainer else Color(
                        0xFFFF8D18
                    ),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        SelectionContainer(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = reviews[0].description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (reviews.size >= 2) {
            MainButton(
                modifier = Modifier
                    .padding(top = 12.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                text = stringResource(id = R.string.see_more),
                isWightBtn = true,
                onClick = onSeeMoreClicked
            )
        }

        MainButton(
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            text = stringResource(id = R.string.review),
            onClick = onReviewClicked
        )
    }
}

@Composable
private fun IncludedArtifactsSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.included_artifacts),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )

        // TODO: Replace with the main item!!
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(count = 10) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(140.dp)
                        .height(165.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}

@Composable
private fun RelatedPlacesSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.related_places),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )

        // TODO: Replace with the main item!!
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(count = 10) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(140.dp)
                        .height(165.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}