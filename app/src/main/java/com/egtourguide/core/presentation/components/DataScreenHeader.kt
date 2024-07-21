package com.egtourguide.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun DataScreenHeaderPreview() {
    EGTourGuideTheme {
        DataScreenHeader(
            title = stringResource(id = R.string.landmarks_count, 55),
            onFilterClicked = {},
            hasChanged = false
        )
    }
}

@Composable
fun DataScreenHeader(
    title: String,
    onFilterClicked: () -> Unit,
    hasChanged: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onFilterClicked()
                },
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(id = R.string.filters),
            tint = if (hasChanged) MaterialTheme.colorScheme.outlineVariant
            else MaterialTheme.colorScheme.onBackground
        )
    }
}