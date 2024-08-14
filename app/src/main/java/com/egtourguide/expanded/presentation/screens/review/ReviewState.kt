package com.egtourguide.expanded.presentation.screens.review

data class ReviewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isRatingError: Boolean = false,
    val isError: Boolean = false,
    val isNetworkError: Boolean = false,
    val rating: Int = 0,
    val review: String = ""
)