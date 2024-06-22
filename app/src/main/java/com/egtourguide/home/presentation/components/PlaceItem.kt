package com.egtourguide.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.LANDMARK_IMAGE_LINK_PREFIX
import com.egtourguide.home.domain.model.Place

@Composable
fun PlaceItem(
    place: Place,
    onPlaceClicked: (Place) -> Unit,
    onSaveClicked: (Place) -> Unit
) {
    Column(
        modifier = Modifier
            .width(144.dp)
            .height(205.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onPlaceClicked(place)
            }
    ) {
        MainImage(
            modifier = Modifier
                .height(105.dp)
                .clip(RoundedCornerShape(4.dp)),
            data = "$LANDMARK_IMAGE_LINK_PREFIX${place.image}"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.height(36.dp),
            text = place.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_location),
                    tint = Color.Unspecified,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = " ${place.location}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    onSaveClicked(place)
                }
            ) {
                Icon(
                    painter = painterResource(id = if (place.isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                    contentDescription = stringResource(id = if (place.isSaved) R.string.unsave else R.string.save),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = 2.dp),
                painter = painterResource(id = R.drawable.ic_rating_star),
                tint = Color.Unspecified,
                contentDescription = "Location Icon"
            )
            Text(
                text = "%.2f".format(place.rating),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.reviews_count, place.ratingCount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun PlaceItemPreview() {
    EGTourGuideTheme {
        PlaceItem(
            place = Place(
                id = "",
                name = "Pyramids",
                image = "https://www.worldhistory.org/uploads/images/5687.jpg",
                location = "Giza",
                isSaved = false,
                rating = 4.576f,
                ratingCount = 72
            ),
            onPlaceClicked = {},
            onSaveClicked = {},
        )
    }
}