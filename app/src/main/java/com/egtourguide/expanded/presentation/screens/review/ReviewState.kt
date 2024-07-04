package com.egtourguide.expanded.presentation.screens.review

data class ReviewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val rating: Int = 0,
    val review: String = ""
)