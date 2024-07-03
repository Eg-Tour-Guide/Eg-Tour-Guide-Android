package com.egtourguide.expanded.presentation.screens.expanded

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX

@Preview
@Composable
private fun AddDialogPreview() {
    EGTourGuideTheme {
        AddDialog(
            tourImage = "",
            tourName = "Test",
            navigateToTours = {},
            onCancelClicked = {},
            onAddClicked = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(
    tourImage: String,
    tourName: String,
    navigateToTours: () -> Unit,
    onCancelClicked: () -> Unit,
    onAddClicked: (Int) -> Unit,
) {
    var value by remember { mutableFloatStateOf(0.0f) }
    var trackWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val sliderRange = 0f..12f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.tour),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayMedium
        )

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    navigateToTours()
                }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (tourName.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainImage(
                        data = "$LANDMARK_IMAGE_LINK_PREFIX$tourImage",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Text(
                        text = tourName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(R.string.choose_tour),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Text(
            text = stringResource(id = R.string.please_pick_the_tour),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.duration),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = stringResource(id = R.string.hours, value.toInt()),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Slider(
            value = value,
            valueRange = 0f..12f,
            onValueChange = { value = it },
            steps = 11,
            colors = SliderDefaults.colors(
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
            ),
            track = { state ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            trackWidth = with(density) { coordinates.size.width.toDp() }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )

                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .width(trackWidth * (state.value / sliderRange.endInclusive))
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        )

        Text(
            text = stringResource(id = R.string.please_choose_the_duration),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(33.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        onCancelClicked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(33.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        onAddClicked(value.toInt())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}