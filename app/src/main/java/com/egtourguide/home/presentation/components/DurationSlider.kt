package com.egtourguide.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun DurationSlider(onValueChange: (Int) -> Unit = {}) {
    var sliderPosition by remember { mutableFloatStateOf(7f) }
    val sliderRange = 0f..30f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            val bubbleOffset =
                (sliderPosition / sliderRange.endInclusive) * (280.dp) // Adjust the width value according to your Slider width
            SliderBubble(
                offset = bubbleOffset,
                value = "${sliderPosition.toInt()} Days"
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                onValueChange(newValue.toInt())
            },

            valueRange = sliderRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
    }
}

@Composable
fun SliderBubble(offset: Dp, value: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset(x = offset - 150.dp) // Adjust for the width of the bubble
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RangeSliderWithBubble(onValueChange: (Int,Int) -> Unit ) {
    var sliderPosition by remember { mutableStateOf(0f..15f) }
    val sliderRange = 0f..30f
    var showBubble by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            if (showBubble) {
                val startBubbleOffset = (sliderPosition.start / sliderRange.endInclusive) * (280.dp) // Adjust the width value according to your Slider width
                val endBubbleOffset = (sliderPosition.endInclusive / sliderRange.endInclusive) * (280.dp) // Adjust the width value according to your Slider width
                RangeSliderBubble(
                    offsetStart = startBubbleOffset,
                    offsetEnd = endBubbleOffset,
                    valueStart = "${sliderPosition.start.toInt()} Days",
                    valueEnd = "${sliderPosition.endInclusive.toInt()} Days"
                )
            }
        }
        RangeSlider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                showBubble = true
                onValueChange(newValue.start.toInt(),newValue.endInclusive.toInt())
            },
            onValueChangeFinished = {
                showBubble = false
            },
            valueRange = sliderRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp) // Adjust the height to make the slider thicker
        )
    }
}

@Composable
private fun RangeSliderBubble(offsetStart: Dp, offsetEnd: Dp, valueStart: String, valueEnd: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = offsetStart - 20.dp) // Adjust for the width of the bubble
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            Text(
                text = valueStart,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = offsetEnd - 250.dp) // Adjust for the width of the bubble
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            Text(
                text = valueEnd,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EGTourGuideTheme {
        RangeSliderWithBubble(){a,b->

        }
    }
}