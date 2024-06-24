package com.egtourguide.home.presentation.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.data.dto.body.ReviewRequestBody
import com.egtourguide.home.domain.usecases.ReviewPlaceUseCase
import com.egtourguide.home.domain.usecases.ReviewTourUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val sendReviewTourUseCase: ReviewTourUseCase,
    private val sendReviewPlaceUseCase: ReviewPlaceUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewState())
    val uiState = _uiState.asStateFlow()

    fun tourOrPlace(tour: Boolean, place: Boolean, id: String) {
        _uiState.update { it.copy(id = id) }
        if (tour) _uiState.update { it.copy(isTour = true) }
        if (place) _uiState.update { it.copy(isPlace = true) }
    }

    fun changeReview(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun changeRating(rating: Float) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }

    fun onSubmitClick() {
        _uiState.update {
            it.copy(
                isLoading = false,
                isSuccess = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val requestBody =
                ReviewRequestBody(_uiState.value.review, _uiState.value.rating.toInt().toString())
            val id = _uiState.value.id

            if (_uiState.value.isTour) {
                sendReviewTourUseCase(id, requestBody).onResponse(
                    onLoading = {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    },
                    onFailure = { _ ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                            )
                        }
                    },
                    onSuccess = { _ ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                )
            }
            if (_uiState.value.isPlace) {
                sendReviewPlaceUseCase(id, requestBody).onResponse(
                    onLoading = {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    },
                    onFailure = { _ ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                            )
                        }
                    },
                    onSuccess = { _ ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                )
            }
        }
    }
}