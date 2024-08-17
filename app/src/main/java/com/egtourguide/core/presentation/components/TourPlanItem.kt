package com.egtourguide.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.expanded.domain.model.TourDetailsPlace

@Preview
@Composable
fun TourPlanItemPreview() {
    EGTourGuideTheme {
        TourPlanItem(
            place = TourDetailsPlace(
                id = "",
                image = "",
                title = "Pyramids",
                govName = "Giza",
                ratingAverage = 4.5,
                ratingQuantity = 72,
                duration = 3
            ),
            onClick = {}
        )
    }
}

@Composable
fun TourPlanItem(
    modifier: Modifier = Modifier,
    place: TourDetailsPlace,
    onClick: (String) -> Unit,
    isCustom: Boolean = false,
    onDeleteClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onClick(place.id)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainImage(
            data = "$LANDMARK_IMAGE_LINK_PREFIX${place.image}",
            contentDescription = null,
            placeholderImage = R.drawable.ic_plan_item,
            errorImage = R.drawable.ic_plan_item,
            modifier = Modifier
                .width(125.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                DataRow(
                    icon = R.drawable.ic_location,
                    iconDescription = stringResource(R.string.location),
                    text = place.govName,
                    iconPadding = 4.dp,
                    iconSize = 10.dp,
                    textStyle = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )

                DataRow(
                    icon = R.drawable.ic_rating_star,
                    iconDescription = null,
                    text = stringResource(
                        id = R.string.reviews_average_total,
                        place.ratingAverage,
                        place.ratingQuantity
                    ),
                    iconPadding = 4.dp,
                    iconSize = 10.dp,
                    iconTint = Color(0xFFFF8D18),
                    textStyle = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )

                DataRow(
                    icon = R.drawable.ic_timesheet,
                    iconDescription = stringResource(R.string.duration),
                    text = stringResource(id = R.string.hours_count, place.duration),
                    iconPadding = 4.dp,
                    iconSize = 10.dp,
                    textStyle = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            if (isCustom) {
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.remove_this_place),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .width(16.dp)
                            .height(20.dp)
                    )
                }
            }
        }
    }
}