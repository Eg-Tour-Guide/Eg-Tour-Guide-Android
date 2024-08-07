package com.egtourguide.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import kotlinx.coroutines.delay

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 50.dp
) {
    val loadingTextList = listOf(
        stringResource(id = R.string.loading),
        stringResource(id = R.string.please_wait),
        stringResource(id = R.string.just_a_moment),
        stringResource(id = R.string.almost_there),
        stringResource(id = R.string.we_re_on_it),
        stringResource(id = R.string.just_a_second_more),
        stringResource(id = R.string.getting_everything_ready)
    )
    var currentLoadingText by remember { mutableStateOf(loadingTextList[0]) }

    LaunchedEffect(key1 = Unit) {
        loadingTextList.forEach { text ->
            delay(1500)
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
            strokeCap = StrokeCap.Round
        )

        Text(
            text = currentLoadingText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}