package com.egtourguide.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 50.dp
) {
    var currentLoadingText by remember { mutableStateOf("Loading...") }
    val loadingTextList = listOf(
        "Please wait...",
        "Just a moment...",
        "Almost there...",
        "We're on it..",
        "Just a second more...",
        "Getting everything ready..."
    )

    LaunchedEffect(key1 = Unit) {
        loadingTextList.forEach { text ->
            delay(2000)
            currentLoadingText = text
        }
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(indicatorSize),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = currentLoadingText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}