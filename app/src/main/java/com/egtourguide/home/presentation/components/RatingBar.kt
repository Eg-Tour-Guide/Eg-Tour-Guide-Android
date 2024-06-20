package com.egtourguide.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    initialRating: Float = 3.0f,
    starSize: Dp = 24.dp,
    isClickable: Boolean = true,
    onRatingChanged: (Float) -> Unit
) {
    var rating by remember {
        mutableFloatStateOf(initialRating)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ) {
        (1..5).forEach { index ->
            StarIcon(
                tint = if (rating >= index) Color(0xFFFF8D18) else Color(0xFFE6E6E6),
                modifier = Modifier
                    .then(
                        if (isClickable) {
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) {
                                rating = index.toFloat()
                                onRatingChanged(rating)
                            }
                        } else {
                            Modifier
                        }
                    ),
                size = starSize

            )
        }
    }
}

@Composable
private fun StarIcon(
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp
) {
    val starIcon = painterResource(id = R.drawable.ic_rating_star)
    Icon(
        painter = starIcon,
        contentDescription = null,
        modifier = modifier
            .padding(end = 8.dp)
            .size(size),
        tint = tint
    )
}


@Preview(showBackground = true)
@Composable
private fun RatingBarReview() {
    EGTourGuideTheme {
        RatingBar(onRatingChanged = {})
    }
}