package com.egtourguide.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview(showBackground = true)
@Composable
private fun MainButtonPreview() {
    EGTourGuideTheme {
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = "Test",
            isLoading = true
        )
    }
}

// TODO: Implement This!!
@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.displayMedium
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isLoading) MaterialTheme.colorScheme.outline
                else MaterialTheme.colorScheme.primary
            )
            .clickable(
                enabled = !isLoading,
                onClick = onClick,
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = textStyle
            )
        }
    }
}