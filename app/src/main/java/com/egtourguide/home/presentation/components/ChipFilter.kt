package com.egtourguide.home.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun ChipFilter(
    modifier: Modifier = Modifier,
    text: String,
    isRating: Boolean = false,
    selected: Boolean = false,
    reset:Boolean=false,
    addSelectedFilter:(String)->Unit,
    removeSelectedFilter:(String)->Unit
) {
    var isSelected by remember {
        if (reset) {
            mutableStateOf(false)
        }else{
            mutableStateOf(selected)
        }
    }
    FilterChip(
        selected = isSelected,
        onClick = {
            if(isSelected){
                addSelectedFilter(text)
                Log.d("````TAG````", "ChipFilter: $text")
            }else{
                removeSelectedFilter(text)
            }
            isSelected = !isSelected
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (!isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.onPrimaryContainer,
            labelColor = if (!isSelected) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.onPrimary,
            selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
//            selectedLeadingIconColor = Color.Yellow,
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        label = {
            if (!isRating) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                )
            } else {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = stringResource(id = R.string.star),
                        tint = Color(0xFFFF8D18),
                        modifier = Modifier
                            .size(14.dp)
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(33.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ChipFilterReview() {
    EGTourGuideTheme {
        ChipFilter(text = "Hi", selected = false, isRating = false, addSelectedFilter = {}, removeSelectedFilter = {})
    }
}