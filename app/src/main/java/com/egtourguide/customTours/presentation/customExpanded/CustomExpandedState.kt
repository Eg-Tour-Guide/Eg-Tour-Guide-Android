package com.egtourguide.customTours.presentation.customExpanded

data class CustomExpandedState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val callIsSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSaveCall: Boolean = false,
    val id: String = "",
    val images: List<String> = emptyList(),
    val title: String = "",
    val duration: Int = 0,
    val isSaved: Boolean = false,
    val description: String = "",
)
