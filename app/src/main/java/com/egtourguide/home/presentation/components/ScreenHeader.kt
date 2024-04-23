package com.egtourguide.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
                showNotifications = true,
                showNotificationsBadge = true,
                modifier = Modifier.height(61.dp)
            )

            ScreenHeader(
                showBack = true,
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
    showNotifications: Boolean = false,
    showNotificationsBadge: Boolean = false,
    showBack: Boolean = false,
    onBackClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onNotificationsClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showLogo) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(id = R.string.logo),
                modifier = Modifier.padding(start = 16.dp)
            )
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(end = 16.dp)
        ) {
            if (showSearch) {
                IconButton(
                    onClick = onSearchClicked,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.go_to_search),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                }
            }

            if (showNotifications) {
                IconButton(
                    onClick = onNotificationsClicked,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (showNotificationsBadge) R.drawable.ic_notifications_badge
                            else R.drawable.ic_notifications
                        ),
                        contentDescription = stringResource(id = R.string.go_to_notifications),
                        modifier = Modifier
                            .height(18.dp)
                            .width(20.dp)
                    )
                }
            }
        }
    }
}