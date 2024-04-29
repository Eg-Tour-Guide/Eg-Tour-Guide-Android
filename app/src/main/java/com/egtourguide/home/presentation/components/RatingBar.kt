package com.egtourguide.home.presentation.components

import android.content.res.ColorStateList
import android.widget.RatingBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    initialRating: Float = 0f,
    movable: Boolean = true,
    updateRating: (Float) -> Unit = {}
) {
    var currantRating by remember {
        mutableFloatStateOf(initialRating)
    }
    AndroidView(
        factory = { context ->
            RatingBar(context).apply {
                numStars = 5
                stepSize = 0.5f
                rating = currantRating
                setIsIndicator(!movable)
                setOnRatingBarChangeListener { _, newRating, _ ->
                    currantRating = newRating
                    updateRating(
                        currantRating
                    )

                }
                progressTintList = ColorStateList.valueOf(Color(0xFFFF8D18).toArgb())

            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun RatingBarReview() {
    EGTourGuideTheme {
        RatingBar()
    }
}