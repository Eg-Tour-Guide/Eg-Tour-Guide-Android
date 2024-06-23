package com.egtourguide.home.presentation.screens.review

data class ReviewState(
    val rating: Float = 0.0f,
    val review: String = "",
    val id: String = "",
    val isPlace: Boolean = false,
    val isTour: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError:Boolean=false
)