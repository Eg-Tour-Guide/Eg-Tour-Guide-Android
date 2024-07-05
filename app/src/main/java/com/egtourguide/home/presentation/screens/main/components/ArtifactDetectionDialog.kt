package com.egtourguide.home.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.egtourguide.R
import com.egtourguide.core.presentation.components.LoadingState
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun ArtifactsDetectionDialogPreview() {
    EGTourGuideTheme {
        ArtifactDetectionDialog(
            isDetectionLoading = false,
            onDismissRequest = {},
            onGalleryClicked = {},
            onCameraClicked = {}
        )
    }
}

@Composable
fun ArtifactDetectionDialog(
    isDetectionLoading: Boolean,
    onDismissRequest: () -> Unit,
    onGalleryClicked: () -> Unit,
    onCameraClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(152.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isDetectionLoading) {
                LoadingState()
            } else {
                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_camera,
                    title = "Camera",
                    onClick = { onCameraClicked() }
                )

                Spacer(modifier = Modifier.width(16.dp))

                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_gallery,
                    title = "Gallery",
                    onClick = { onGalleryClicked() }
                )
            }
        }
    }
}

@Composable
private fun ArtifactDetectionDialogOption(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 42.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}