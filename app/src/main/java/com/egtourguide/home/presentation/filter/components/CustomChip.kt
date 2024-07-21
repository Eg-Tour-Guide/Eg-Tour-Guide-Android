package com.egtourguide.home.presentation.filter.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    title: String,
    isRating: Boolean = false,
    isSelected: Boolean = false,
    onClicked: (String) -> Unit
) {
    FilterChip(
        modifier = modifier.height(33.dp),
        selected = isSelected,
        onClick = { onClicked(title) },
        border = BorderStroke(0.dp, Color.Unspecified),
        shape = RoundedCornerShape(16.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (!isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.onPrimaryContainer,
            labelColor = if (!isSelected) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.onPrimary,
            selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRating) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = stringResource(id = R.string.star),
                        tint = Color(0xFFFF8D18),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = if (isRating) 2.dp else 0.dp)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ChipFilterReview() {
    EGTourGuideTheme {
        CustomChip(
            title = "Hi",
            isSelected = false,
            isRating = true,
            onClicked = {}
        )
    }
}