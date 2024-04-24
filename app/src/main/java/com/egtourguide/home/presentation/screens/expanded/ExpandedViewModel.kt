package com.egtourguide.home.presentation.screens.expanded

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
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
                        description = LoremIpsum(words = 20).values.iterator().asSequence()
                            .joinToString(" ")
                    )
                ),
                tourismTypes = listOf("Adventure", "Historical"),
                description = LoremIpsum(words = 50).values.iterator().asSequence()
                    .joinToString(" ")
            )
        }
    }

    fun changeSavedState() {
        _uiState.update { it.copy(isSaved = !it.isSaved) }
    }
}