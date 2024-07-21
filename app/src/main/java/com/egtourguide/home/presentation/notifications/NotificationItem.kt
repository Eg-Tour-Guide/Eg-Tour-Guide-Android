package com.egtourguide.home.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.getLoremString

@Preview
@Composable
private fun NotificationItemPreview() {
    EGTourGuideTheme {
        NotificationItem(
            notification = Notification(
                id = "1",
                title = "Title",
                body = getLoremString(15),
                isRead = false
            )
        )
    }
}

@Composable
fun NotificationItem(
    notification: Notification
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    if (notification.isRead) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.error
                )
        )

        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = notification.body,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}