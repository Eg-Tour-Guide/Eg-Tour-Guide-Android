package com.egtourguide.expanded.presentation.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        _uiState.update { it.copy(review = review) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onSubmitClick(isLandMark: Boolean, id: String) {
        // TODO: The user can review with 0 or not!!
        if (isLandMark) reviewLandmark(id = id)
        else reviewTour(id = id)
    }

    private fun reviewLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewPlaceUseCase(
                id = id,
                rating = _uiState.value.rating,
                review = _uiState.value.review
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
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
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }
}