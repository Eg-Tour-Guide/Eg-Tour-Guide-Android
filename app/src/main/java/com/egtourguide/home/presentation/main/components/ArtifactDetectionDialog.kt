package com.egtourguide.home.presentation.main.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            isDetectionLoading = true,
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
        onDismissRequest = {
            if (!isDetectionLoading) onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isDetectionLoading) {
                LoadingState(modifier = Modifier.weight(1f))
            } else {
                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_camera,
                    title = stringResource(id = R.string.camera),
                    onClick = onCameraClicked,
                    modifier = Modifier.weight(1f)
                )

                ArtifactDetectionDialogOption(
                    icon = R.drawable.ic_gallery,
                    title = stringResource(id = R.string.gallery),
                    onClick = onGalleryClicked,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ArtifactDetectionDialogOption(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null,
            modifier = Modifier
                .width(56.dp)
                .height(50.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}