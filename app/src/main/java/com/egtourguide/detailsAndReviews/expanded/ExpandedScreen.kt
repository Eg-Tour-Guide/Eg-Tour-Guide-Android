package com.egtourguide.detailsAndReviews.expanded

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.components.MapItem
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.ARTIFACT_IMAGE_LINK_PREFIX
import com.egtourguide.core.utils.Constants.AR_MODEL_LINK_PREFIX
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.model.Review
import com.egtourguide.home.presentation.components.ArtifactItem
import com.egtourguide.home.presentation.components.DataRow
import com.egtourguide.home.presentation.components.LoadingState
import com.egtourguide.home.presentation.components.PlaceItem
import com.egtourguide.home.presentation.components.ReviewItem
import com.egtourguide.home.presentation.components.ReviewsHeader
import com.egtourguide.home.presentation.components.ScreenHeader
import com.egtourguide.home.presentation.components.TourItem
import com.egtourguide.home.presentation.utils.convertDate

@Preview(showBackground = true, heightDp = 2000)
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
                        id = "",
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 2,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        id = "",
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 2,
                        description = getLoremString(words = 20)
                    )
                ),
                tourismTypes = "Adventure, Historical",
                description = getLoremString(words = 50),
                artifactType = "Statues",
                artifactMaterials = "Stone, Wood",
                vrModel = "test"
            )
        )
    }
}

@Composable
fun ExpandedScreenRoot(
    viewModel: ExpandedViewModel = hiltViewModel(),
    id: String,
    tourId: String,
    tourName: String,
    tourImage: String,
    expandedType: String,
    onBackClicked: () -> Unit,
    onSeeMoreClicked: () -> Unit,
    onReviewClicked: () -> Unit,
    navigateToWebScreen: (String) -> Unit,
    navigateToTours: () -> Unit,
    goToTourPlan: (String) -> Unit,
    navigateToSingleItem: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getData(id = id, expandedType = expandedType)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = tourId) {
        viewModel.changeTourData(id = tourId, name = tourName, image = tourImage)
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.showAddSuccess) {
        if (uiState.showAddSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.added_successfully),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(key1 = uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            Toast.makeText(
                context,
                if (uiState.isSaveCall) R.string.saved_successfully else R.string.unsaved_successfully,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSaveSuccess()
        }
    }

    if (uiState.showAddDialog) {
        Dialog(onDismissRequest = viewModel::changeAddDialogVisibility) {
            AddDialog(
                tourImage = uiState.tourImage,
                tourName = uiState.tourName,
                navigateToTours = {
                    navigateToTours()
                },
                onCancelClicked = viewModel::changeAddDialogVisibility,
                onAddClicked = { duration ->
                    viewModel.addToTour(duration)
                    viewModel.changeAddDialogVisibility()
                }
            )
        }
    }

    if (uiState.showLoadingDialog) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {
                LoadingState()
            }
        }
    }

    ExpandedScreen(
        expandedType = expandedType,
        uiState = uiState,
        onBackClicked = onBackClicked,
        onVrViewClicked = {
            navigateToWebScreen(uiState.vrModel)
        },
        onArViewClicked = {
            val uri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter(
                    "file",
                    "$AR_MODEL_LINK_PREFIX${uiState.arModel}"
                )
                .appendQueryParameter("mode", "ar_preferred")
                .appendQueryParameter("title", uiState.title)
                .build()

            Intent(Intent.ACTION_VIEW).also { sceneViewerIntent ->
                sceneViewerIntent.setData(uri)
                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
                context.startActivity(sceneViewerIntent)
            }
        },
        onSaveClicked = viewModel::changeSavedState,
        onSavePlace = viewModel::changePlaceSavedState,
        onSaveArtifact = viewModel::changeArtifactSavedState,
        onSaveTour = viewModel::changeTourSavedState,
        onAddClicked = viewModel::changeAddDialogVisibility,
        onSeeMoreClicked = onSeeMoreClicked,
        onReviewClicked = onReviewClicked,
        goToTourPlan = { goToTourPlan(uiState.id) },
        navigateToSingleItem = navigateToSingleItem
    )
}

@Composable
private fun ExpandedScreen(
    expandedType: String = ExpandedType.LANDMARK.name,
    uiState: ExpandedScreenState,
    onBackClicked: () -> Unit = {},
    onArViewClicked: () -> Unit = {},
    onVrViewClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onSavePlace: (Place) -> Unit = {},
    onSaveArtifact: (AbstractedArtifact) -> Unit = {},
    onSaveTour: (AbstractedTour) -> Unit = {},
    onAddClicked: () -> Unit = {},
    goToTourPlan: () -> Unit = {},
    onSeeMoreClicked: () -> Unit = {},
    onReviewClicked: () -> Unit = {},
    navigateToSingleItem: (String, String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            showBack = true,
            showArView = (expandedType == ExpandedType.ARTIFACT.name && uiState.arModel.isNotEmpty()),
            showVrView = (expandedType == ExpandedType.LANDMARK.name && uiState.vrModel.isNotEmpty()),
            onArViewClicked = onArViewClicked,
            onVrViewClicked = onVrViewClicked,
            onBackClicked = onBackClicked,
            modifier = Modifier.height(52.dp)
        )

        if (uiState.isLoading) {
            LoadingState(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    ImagesSection(
                        images = uiState.images,
                        imageLinkPrefix = if (expandedType == ExpandedType.ARTIFACT.name) ARTIFACT_IMAGE_LINK_PREFIX
                        else LANDMARK_IMAGE_LINK_PREFIX,
                        title = uiState.title
                    )
                }

                item {
                    TitleSection(
                        expandedType = expandedType,
                        title = uiState.title,
                        isSaved = uiState.isSaved,
                        onSaveClicked = onSaveClicked,
                        onAddClicked = onAddClicked,
                        goToTourPlan = goToTourPlan,
                        location = uiState.location,
                        reviewsAverage = uiState.reviewsAverage,
                        reviewsCount = uiState.reviewsCount,
                        tourismTypes = uiState.tourismTypes,
                        artifactType = uiState.artifactType,
                        artifactMaterials = uiState.artifactMaterials,
                        date = uiState.date,
                        duration = uiState.duration,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                if (uiState.description.isNotEmpty()) {
                    item {
                        DescriptionSection(
                            description = uiState.description,
                            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
                        )
                    }
                }

                if (expandedType != ExpandedType.TOUR.name && expandedType != ExpandedType.CUSTOM_TOUR.name && uiState.latitute != 0.0 && uiState.longitude != 0.0) {
                    item {
                        LocationSection(
                            title = uiState.title,
                            latitude = uiState.latitute,
                            longitude = uiState.longitude,
                            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
                        )
                    }
                }

                if (expandedType == ExpandedType.LANDMARK.name || expandedType == ExpandedType.TOUR.name) {
                    item {
                        ReviewsSection(
                            reviewsAverage = uiState.reviewsAverage,
                            reviewsCount = uiState.reviewsCount,
                            reviews = uiState.reviews,
                            onSeeMoreClicked = onSeeMoreClicked,
                            onReviewClicked = onReviewClicked,
                            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
                        )
                    }
                }

                if (uiState.includedArtifacts.isNotEmpty()) {
                    item {
                        IncludedArtifactsSection(
                            artifacts = uiState.includedArtifacts,
                            onArtifactClicked = navigateToSingleItem,
                            onSaveClicked = onSaveArtifact,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }

                if (uiState.relatedPlaces.isNotEmpty()) {
                    item {
                        RelatedPlacesSection(
                            places = uiState.relatedPlaces,
                            onPlaceClicked = navigateToSingleItem,
                            onSaveClicked = onSavePlace,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }

                if (uiState.relatedArtifacts.isNotEmpty()) {
                    item {
                        RelatedArtifactsSection(
                            artifacts = uiState.relatedArtifacts,
                            onArtifactClicked = navigateToSingleItem,
                            onSaveClicked = onSaveArtifact,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }

                if (uiState.relatedTours.isNotEmpty()) {
                    item {
                        RelatedToursSection(
                            artifacts = uiState.relatedTours,
                            onArtifactClicked = navigateToSingleItem,
                            onSaveClicked = onSaveTour,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ImagesSection(
    images: List<String>,
    imageLinkPrefix: String,
    title: String
) {
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
            MainImage(
                data = "$imageLinkPrefix${images[it]}",
                contentDescription = stringResource(id = R.string.image_num, title, it + 1),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(246.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds
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
    expandedType: String,
    title: String,
    isSaved: Boolean,
    onSaveClicked: () -> Unit,
    onAddClicked: () -> Unit,
    goToTourPlan: () -> Unit,
    location: String,
    date: String,
    duration: Int,
    reviewsAverage: Double,
    reviewsCount: Int,
    tourismTypes: String,
    artifactType: String,
    artifactMaterials: String,
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                if (location.isNotEmpty()) {
                    DataRow(
                        icon = R.drawable.ic_location,
                        iconDescription = stringResource(R.string.location),
                        text = location,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (expandedType == ExpandedType.EVENT.name && date.isNotEmpty()) {
                    DataRow(
                        icon = R.drawable.ic_calendar,
                        iconDescription = stringResource(R.string.date),
                        text = convertDate(inputDate = date),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                if (expandedType != ExpandedType.LANDMARK.name && expandedType != ExpandedType.ARTIFACT.name && duration != 0) {
                    DataRow(
                        icon = R.drawable.ic_timesheet,
                        iconDescription = stringResource(R.string.duration),
                        text = stringResource(id = R.string.days, duration),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (expandedType == ExpandedType.EVENT.name) 8.dp else 0.dp)
                    )
                }

                if (expandedType == ExpandedType.LANDMARK.name || expandedType == ExpandedType.TOUR.name) {
                    DataRow(
                        icon = R.drawable.ic_rating_star,
                        iconDescription = null,
                        text = stringResource(
                            id = R.string.reviews_average_total,
                            reviewsAverage,
                            reviewsCount
                        ),
                        iconTint = Color(0xFFFF8D18),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                if (tourismTypes.isNotEmpty()) {
                    DataRow(
                        icon = R.drawable.ic_landmarks,
                        iconDescription = stringResource(R.string.tourism_types),
                        text = tourismTypes,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                if (artifactType.isNotEmpty()) {
                    DataRow(
                        icon = R.drawable.ic_artifacts,
                        iconDescription = stringResource(R.string.artifact_type),
                        text = artifactType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                if (artifactMaterials.isNotEmpty()) {
                    DataRow(
                        icon = R.drawable.ic_materials,
                        iconDescription = stringResource(R.string.artifact_materials),
                        text = artifactMaterials,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }

            if (expandedType != ExpandedType.EVENT.name) {
                Column(
                    modifier = Modifier.padding(end = 17.dp, start = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onSaveClicked,
                        modifier = Modifier.size(31.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                            contentDescription = stringResource(id = if (isSaved) R.string.unsave else R.string.save),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (expandedType == ExpandedType.LANDMARK.name) {
                        IconButton(
                            onClick = onAddClicked,
                            modifier = Modifier.size(31.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_to_tour),
                                contentDescription = stringResource(id = R.string.add_to_tour),
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    if (expandedType == ExpandedType.TOUR.name || expandedType == ExpandedType.CUSTOM_TOUR.name) {
                        IconButton(
                            onClick = goToTourPlan,
                            modifier = Modifier.size(31.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_timesheet),
                                contentDescription = stringResource(id = R.string.tour_schedule),
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
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
    title: String,
    latitude: Double,
    longitude: Double,
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

        MapItem(
            title = title,
            latitude = latitude,
            longitude = longitude,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(125.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun ReviewsSection(
    reviewsAverage: Double,
    reviewsCount: Int,
    reviews: List<Review>,
    onSeeMoreClicked: () -> Unit,
    onReviewClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ReviewsHeader(
            reviewsAverage = reviewsAverage,
            reviewsTotal = reviewsCount
        )

        if (reviews.isNotEmpty()) {
            ReviewItem(
                review = reviews[0],
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        if (reviews.size >= 2) {
            MainButton(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                text = stringResource(id = R.string.see_more),
                isWightBtn = true,
                onClick = onSeeMoreClicked
            )
        }

        MainButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(40.dp),
            text = stringResource(id = R.string.review),
            onClick = onReviewClicked
        )
    }
}

@Composable
private fun IncludedArtifactsSection(
    artifacts: List<AbstractedArtifact>,
    onSaveClicked: (AbstractedArtifact) -> Unit,
    onArtifactClicked: (String, String) -> Unit,
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(items = artifacts, key = { it.id }) {
                ArtifactItem(
                    artifact = it,
                    onArtifactClicked = { artifact ->
                        onArtifactClicked(artifact.id, ExpandedType.ARTIFACT.name)
                    },
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun RelatedPlacesSection(
    places: List<Place>,
    onPlaceClicked: (String, String) -> Unit,
    onSaveClicked: (Place) -> Unit,
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(items = places, key = { it.id }) {
                PlaceItem(
                    place = it,
                    onPlaceClicked = { place ->
                        onPlaceClicked(place.id, ExpandedType.LANDMARK.name)
                    },
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun RelatedArtifactsSection(
    artifacts: List<AbstractedArtifact>,
    onArtifactClicked: (String, String) -> Unit,
    onSaveClicked: (AbstractedArtifact) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.related_artifacts),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(items = artifacts, key = { it.id }) {
                ArtifactItem(
                    artifact = it,
                    onArtifactClicked = { artifact ->
                        onArtifactClicked(artifact.id, ExpandedType.ARTIFACT.name)
                    },
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}

@Composable
private fun RelatedToursSection(
    artifacts: List<AbstractedTour>,
    onArtifactClicked: (String, String) -> Unit,
    onSaveClicked: (AbstractedTour) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.related_tours),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(items = artifacts, key = { it.id }) {
                TourItem(
                    tour = it,
                    onTourClicked = { tour ->
                        onArtifactClicked(tour.id, ExpandedType.TOUR.name)
                    },
                    onSaveClicked = onSaveClicked
                )
            }
        }
    }
}