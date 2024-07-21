package com.egtourguide.expanded.presentation.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.core.utils.onResponse
import com.egtourguide.expanded.domain.usecases.ReviewPlaceUseCase
import com.egtourguide.expanded.domain.usecases.ReviewTourUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewTourUseCase: ReviewTourUseCase,
    private val reviewPlaceUseCase: ReviewPlaceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState = _uiState.asStateFlow()

    fun changeRating(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun changeReview(review: String) {
        _uiState.update { it.copy(review = review, isRatingError = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = false, isRatingError = false) }
    }

    fun onSubmitClick(isLandMark: Boolean, id: String) {
        if (_uiState.value.rating == 0) {
            _uiState.update { it.copy(isRatingError = true) }
        } else {
            if (isLandMark) reviewLandmark(id = id)
            else reviewTour(id = id)
        }
    }

    private fun reviewLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewPlaceUseCase(
                id = id,
                rating = _uiState.value.rating,
                review = _uiState.value.review
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = false) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, error = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, error = true) }
                }
            )
        }
    }

    private fun reviewTour(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewTourUseCase(
                id = id,
                rating = _uiState.value.rating,
                review = _uiState.value.review
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = false) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, error = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, error = true) }
                }
            )
        }
    }
}
