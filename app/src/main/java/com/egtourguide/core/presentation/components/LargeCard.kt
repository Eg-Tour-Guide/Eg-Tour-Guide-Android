package com.egtourguide.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ItemType
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.ARTIFACT_IMAGE_LINK_PREFIX
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX

@Preview
@Composable
private fun LargeCardPreview() {
    EGTourGuideTheme {
        LargeCard(
            image = "",
            isSaved = false,
            name = "Pyramids",
            ratingAverage = 4.5,
            duration = 3,
            location = "Giza",
            ratingCount = 72,
            artifactType = "Statues",
            itemType = ItemType.TOUR,
            onItemClicked = {},
            onSaveClicked = {},
            modifier = Modifier
                .width(156.dp)
                .height(178.dp)
        )
    }
}

@Composable
fun LargeCard(
    modifier: Modifier = Modifier,
    itemType: ItemType,
    image: String,
    name: String,
    isSaved: Boolean,
    onItemClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    duration: Int = 0,
    location: String = "",
    ratingAverage: Double = 0.0,
    ratingCount: Int = 0,
    artifactType: String = ""
) {
    var isSavedState by remember { mutableStateOf(isSaved) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onItemClicked() }
            .padding(8.dp)
    ) {
        MainImage(
            modifier = Modifier
                .height(105.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            data = "${if (itemType == ItemType.ARTIFACT) ARTIFACT_IMAGE_LINK_PREFIX else LANDMARK_IMAGE_LINK_PREFIX}$image",
            placeHolderImage = R.drawable.large_card_image,
            errorImage = R.drawable.large_card_image
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (itemType == ItemType.TOUR) {
                    DataRow(
                        icon = R.drawable.ic_timesheet,
                        iconPadding = 4.dp,
                        iconSize = 10.dp,
                        iconDescription = stringResource(id = R.string.duration),
                        iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = stringResource(id = R.string.days, duration),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                } else {
                    DataRow(
                        icon = R.drawable.ic_location,
                        iconPadding = 4.dp,
                        iconSize = 10.dp,
                        iconDescription = stringResource(id = R.string.location),
                        iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = location,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        textStyle = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                if (itemType != ItemType.ARTIFACT) {
                    DataRow(
                        icon = R.drawable.ic_rating_star,
                        iconPadding = 4.dp,
                        iconSize = 10.dp,
                        text = stringResource(
                            id = R.string.reviews_average_total,
                            ratingAverage,
                            ratingCount
                        ),
                        iconTint = Color(0xFFFF8D18),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                } else {
                    DataRow(
                        icon = R.drawable.ic_artifacts,
                        iconPadding = 4.dp,
                        iconSize = 10.dp,
                        iconDescription = stringResource(id = R.string.artifact_type),
                        iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = artifactType,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        textStyle = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(id = if (isSavedState) R.drawable.ic_saved else R.drawable.ic_save),
                contentDescription = stringResource(id = if (isSavedState) R.string.unsave else R.string.save),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(end = 2.dp, top = 8.dp)
                    .height(14.dp)
                    .width(11.dp)
                    .clickable {
                        isSavedState = !isSavedState
                        onSaveClicked()
                    }
            )
        }
    }
}