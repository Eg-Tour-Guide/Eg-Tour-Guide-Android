package com.egtourguide.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
private fun ScreenHeaderPreview() {
    EGTourGuideTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(16.dp)
        ) {
            ScreenHeader(
                showLogo = true,
                showSearch = true,
                showCaptureObject = true,
                modifier = Modifier.height(61.dp)
            )

            ScreenHeader(
                showBack = true,
                showArView = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(52.dp)
            )

            ScreenHeader(
                showBack = true,
                showVrView = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(52.dp)
            )

            ScreenHeader(
                showBack = true,
                showEdit = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(52.dp)
            )

            ScreenHeader(
                showBack = true,
                showAdd = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(52.dp)
            )
        }
    }
}

@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    showLogo: Boolean = false,
    showSearch: Boolean = false,
    showCaptureObject: Boolean = false,
    showVrView: Boolean = false,
    showArView: Boolean = false,
    showBack: Boolean = false,
    showAdd: Boolean = false,
    showEdit: Boolean = false,
    onBackClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onCaptureObjectClicked: () -> Unit = {},
    onVrViewClicked: () -> Unit = {},
    onArViewClicked: () -> Unit = {},
    onAddClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showLogo) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(id = R.string.logo),
                    modifier = Modifier
                        .height(40.dp)
                        .width(30.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_text_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(16.dp)
                        .width(119.dp)
                )
            }
        }

        if (showBack) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(end = 16.dp)
        ) {
            if (showSearch) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(id = R.string.go_to_search),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onSearchClicked
                        )
                )
            }

            if (showCaptureObject) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_capture_object),
                    contentDescription = stringResource(id = R.string.detect_object),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onCaptureObjectClicked
                        )
                )
            }

            if (showVrView) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vr_view),
                    contentDescription = stringResource(id = R.string.show_vr_view),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onVrViewClicked
                        )
                )
            }

            if (showArView) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ar_view),
                    contentDescription = stringResource(id = R.string.show_ar_view),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onArViewClicked
                        )
                )
            }

            if (showAdd) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onAddClicked
                        )
                )
            }

            if (showEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.edit_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onEditClicked
                        )
                )
            }
        }
    }
}