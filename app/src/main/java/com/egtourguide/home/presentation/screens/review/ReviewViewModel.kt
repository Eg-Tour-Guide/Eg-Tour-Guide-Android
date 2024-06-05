package com.egtourguide.home.presentation.screens.review

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ReviewViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewState())
    val uiState = _uiState.asStateFlow()
    fun changeReview(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun changeRating(rating: Float) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun onSubmitClick() {
        _uiState.update {
            it.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }
}