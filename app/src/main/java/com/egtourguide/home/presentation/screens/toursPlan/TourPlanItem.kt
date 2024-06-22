package com.egtourguide.home.presentation.screens.toursPlan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.components.DataRow

@Preview
@Composable
fun TourPlanItemPreview() {
    EGTourGuideTheme {
        TourPlanItem(

        )
    }
}

@Composable
fun TourPlanItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainImage(
            data = "",
            contentDescription = null,
            modifier = Modifier
                .width(125.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Pyramids",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            DataRow(
                icon = R.drawable.ic_location,
                iconDescription = stringResource(R.string.location),
                text = "Giza",
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
                    4.5,
                    72
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
                text = "3 hours",
                iconPadding = 4.dp,
                iconSize = 10.dp,
                textStyle = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}