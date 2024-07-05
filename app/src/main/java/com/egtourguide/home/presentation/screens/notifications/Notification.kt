package com.egtourguide.home.presentation.screens.notifications

import com.egtourguide.core.utils.getLoremString

data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val isRead: Boolean
)

val notifications = listOf(
    Notification(
        id = "1",
        title = "Title",
        body = getLoremString(15),
        isRead = false
    ),
    Notification(
        id = "2",
        title = "Title",
        body = getLoremString(10),
        isRead = false
    ),
    Notification(
        id = "3",
        title = "Title",
        body = getLoremString(15),
        isRead = false
    ),
    Notification(
        id = "4",
        title = "Title",
        body = getLoremString(7),
        isRead = false
    ),
    Notification(
        id = "5",
        title = "Title",
        body = getLoremString(15),
        isRead = false
    ),
    Notification(
        id = "6",
        title = "Title",
        body = getLoremString(7),
        isRead = true
    ),
    Notification(
        id = "7",
        title = "Title",
        body = getLoremString(15),
        isRead = true
    ),
    Notification(
        id = "8",
        title = "Title",
        body = getLoremString(15),
        isRead = true
    ),
    Notification(
        id = "9",
        title = "Title",
        body = getLoremString(15),
        isRead = true
    ),
    Notification(
        id = "10",
        title = "Title",
        body = getLoremString(15),
        isRead = true
    )
)