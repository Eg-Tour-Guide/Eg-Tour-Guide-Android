package com.egtourguide.expanded.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    starSize: Dp = 24.dp,
    starPadding: Dp = 8.dp,
    isClickable: Boolean = true,
    onRatingChanged: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(starPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { index ->
            Icon(
                painter = painterResource(id = R.drawable.ic_rating_star),
                contentDescription = null,
                tint = if (rating >= index) Color(0xFFFF8D18)
                else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(starSize)
                    .clickable(
                        enabled = isClickable,
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onRatingChanged(index)
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RatingBarReview() {
    EGTourGuideTheme {
        RatingBar(onRatingChanged = {})
    }
}