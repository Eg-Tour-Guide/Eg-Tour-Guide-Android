package com.egtourguide.customTours.presentation.customExpanded

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.core.presentation.components.DataRow
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.components.NetworkErrorScreen
import com.egtourguide.core.presentation.components.PullToRefreshScreen
import com.egtourguide.core.presentation.components.ScreenHeader

@Preview(showBackground = true)
@Composable
private fun ExpandedScreenPreview() {
    EGTourGuideTheme {
        CustomExpandedContent(
            uiState = CustomExpandedState(
                id = "1",
                images = (1..25).map { "" },
                title = "Pyramids",
                duration = 3,
                description = getLoremString(words = 50)
            )
        )
    }
}

@Composable
fun CustomExpandedScreenRoot(
    viewModel: CustomExpandedViewModel = hiltViewModel(),
    tourId: String,
    onBackClicked: () -> Unit,
    onEditClicked: (String, String) -> Unit,
    goToTourPlan: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    BackHandler {
        onBackClicked()
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !uiState.callIsSent) {
                viewModel.getData(id = tourId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            val message = if (uiState.isSaveCall) R.string.save_success else R.string.unsave_success
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveSuccess()
        }
    }

    LaunchedEffect(key1 = uiState.isSaveError) {
        if (uiState.isSaveError) {
            val message = if (uiState.isSaveCall) R.string.save_error else R.string.unsave_error
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSaveError()
        }
    }

    PullToRefreshScreen(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshData(id = tourId) }
    ) {
        CustomExpandedContent(
            uiState = uiState,
            onBackClicked = onBackClicked,
            onEditClicked = {
                onEditClicked(
                    uiState.title,
                    uiState.description
                )
            },
            onSaveClicked = viewModel::onSaveClicked,
            goToTourPlan = { goToTourPlan(uiState.id) }
        )
    }
}

@Composable
private fun CustomExpandedContent(
    uiState: CustomExpandedState,
    onBackClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    goToTourPlan: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            showEdit = !uiState.isLoading && uiState.id.isNotEmpty(),
            onBackClicked = onBackClicked,
            onEditClicked = onEditClicked,
            modifier = Modifier.height(52.dp)
        )

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState(
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NetworkErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }

        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.id.isNotEmpty() && !uiState.isNetworkError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    ImagesSection(
                        images = uiState.images,
                        title = uiState.title
                    )
                }

                item {
                    DataSection(
                        title = uiState.title,
                        isSaved = uiState.isSaved,
                        onSaveClicked = onSaveClicked,
                        goToTourPlan = goToTourPlan,
                        duration = uiState.duration,
                        description = uiState.description,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ImagesSection(
    images: List<String>,
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
                data = "$LANDMARK_IMAGE_LINK_PREFIX${images[it]}",
                contentDescription = stringResource(id = R.string.image_num, title, it + 1),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(246.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds,
                placeholderImage = R.drawable.ic_expanded,
                errorImage = R.drawable.ic_expanded
            )
        }

        FlowRow(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                if (pagerState.currentPage == iteration) {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp, top = 8.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp, top = 8.dp)
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
private fun DataSection(
    title: String,
    isSaved: Boolean,
    onSaveClicked: () -> Unit,
    goToTourPlan: () -> Unit,
    duration: Int,
    description: String,
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
                if (duration != 0) {
                    DataRow(
                        icon = R.drawable.ic_timesheet,
                        iconDescription = stringResource(R.string.duration),
                        text = stringResource(id = R.string.days_count, duration),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (description.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.description),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

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

        SelectionContainer(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
