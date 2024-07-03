package com.egtourguide.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
fun DataRowPreview() {
    EGTourGuideTheme {
        DataRow(
            icon = R.drawable.ic_location,
            iconDescription = stringResource(id = R.string.location),
            text = "Alexandria"
        )
    }
}

@Composable
fun DataRow(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    iconDescription: String? = null,
    text: String,
    iconSize: Dp = 16.dp,
    iconTint: Color = MaterialTheme.colorScheme.onBackground,
    iconPadding: Dp = 6.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = iconDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )

        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(start = iconPadding)
        )
    }
}