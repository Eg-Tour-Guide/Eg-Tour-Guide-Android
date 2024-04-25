package com.egtourguide.home.presentation.screens.moreReviews

import androidx.lifecycle.ViewModel
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MoreReviewsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MoreReviewsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                reviewsTotal = 100,
                reviewsAverage = 4.5,
                reviews = listOf(
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    ),
                    Review(
                        authorName = "Abdo Sharaf",
                        authorImage = "",
                        rating = 3.5,
                        description = getLoremString(words = 20)
                    )
                )
            )
        }
    }
}