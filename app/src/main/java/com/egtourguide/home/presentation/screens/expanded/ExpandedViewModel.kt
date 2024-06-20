package com.egtourguide.home.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.Review
import com.egtourguide.home.domain.usecases.GetArtifactUseCase
import com.egtourguide.home.domain.usecases.GetLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor(
    private val getLandmarkUseCase: GetLandmarkUseCase,
    private val getArtifactUseCase: GetArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedScreenState())
    val uiState = _uiState.asStateFlow()

    fun getData(id: String, isLandmark: Boolean) {
        if (isLandmark) getLandmark(id = id)
        else getArtifact(id = id)
    }

    private fun getLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, id = id, isLandmark = true) }

            delay(1000L)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    callIsSent = true,
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
                    description = getLoremString(words = 50),
                    includedArtifacts = (1..10).toList(),
                    relatedPlaces = (1..10).toList(),
                    latitute = 29.9772961,
                    longitude = 31.1276246
                )
            }
        }
    }

    private fun getArtifact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, id = id, isLandmark = false) }

            delay(1000L)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    callIsSent = true,
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
                    description = getLoremString(words = 50),
                    artifactType = "Statues",
                    artifactMaterials = listOf("Stone", "Wood"),
                    relatedArtifacts = (1..10).toList()
                )
            }
        }
    }

    fun changeSavedState() {
        _uiState.update { it.copy(isSaved = !it.isSaved) }
    }
}