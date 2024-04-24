package com.egtourguide.home.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                images = listOf("", "", "", ""),
                title = "Pyramids",
                location = "Giza",
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
                ),
                tourismTypes = listOf("Adventure", "Historical"),
                description = getLoremString(words = 50)
            )
        }
    }

    fun changeSavedState() {
        _uiState.update { it.copy(isSaved = !it.isSaved) }
    }
}