package com.egtourguide.home.presentation.filter.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
fun CustomRangeSliderPreview() {
    EGTourGuideTheme {
        var range by remember {
            mutableStateOf(4f..18f)
        }

        CustomRangeSlider(
            min = range.start,
            max = range.endInclusive,
            onDurationChanged = { min, max ->
                range = min..max
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRangeSlider(
    min: Float = 4f,
    max: Float = 18f,
    onDurationChanged: (Float, Float) -> Unit = { _, _ -> }
) {
    RangeSlider(
        value = min..max,
        valueRange = 0f..30f,
        steps = 31,
        onValueChange = {
            onDurationChanged(it.start, it.endInclusive)
        },
        track = { state ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )

                val color = MaterialTheme.colorScheme.onBackground

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                ) {
                    drawLine(
                        color = Color(color.value),
                        start = Offset(
                            x = size.width * (state.activeRangeStart / 30),
                            y = size.height / 2
                        ),
                        end = Offset(
                            x = size.width * (state.activeRangeEnd / 30),
                            y = size.height / 2
                        ),
                        cap = StrokeCap.Round,
                        strokeWidth = size.height
                    )
                }
            }
        }
    )
}