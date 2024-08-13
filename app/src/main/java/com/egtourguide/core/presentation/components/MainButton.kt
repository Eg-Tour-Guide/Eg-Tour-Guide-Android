package com.egtourguide.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview(showBackground = true)
@Composable
private fun MainButtonPreview() {
    EGTourGuideTheme {
        MainButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Test",
            isLoading = true
        )
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    isWightBtn: Boolean = false
) {
    Button(
        modifier = modifier.height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isWightBtn) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isWightBtn) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.outline
        )
    ) {
        if (isLoading) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.button_loading_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(72.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}
