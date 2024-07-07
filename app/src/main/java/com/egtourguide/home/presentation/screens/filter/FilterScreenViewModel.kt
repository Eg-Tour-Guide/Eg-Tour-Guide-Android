package com.egtourguide.home.presentation.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FilterScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FilterScreenState())
    val uiState = _uiState.asStateFlow()

    val hasChanged: StateFlow<Boolean> = _uiState
        .map { it.hasChanged() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setType(filterType: FilterType) {
        _uiState.update { it.copy(filterType = filterType) }
    }

    fun onCategoryChipClicked(category: String) {
        _uiState.update {
            it.copy(selectedCategory = if (category == it.selectedCategory) "" else category)
        }
    }

    fun onTourismTypeChipClicked(tourismType: String) {
        _uiState.update {
            it.copy(
                selectedTourismTypes = if (tourismType in it.selectedTourismTypes) it.selectedTourismTypes - tourismType
                else it.selectedTourismTypes + tourismType
            )
        }
    }

    fun onLocationChipClicked(location: String) {
        _uiState.update {
            it.copy(
                selectedLocations = if (location in it.selectedLocations) it.selectedLocations - location
                else it.selectedLocations + location
            )
        }
    }

    fun onRatingChipClicked(rating: String) {
        _uiState.update {
            it.copy(selectedRating = if (it.selectedRating == rating) "" else rating)
        }
    }

    fun onSortByChipClicked(item: String) {
        _uiState.update {
            it.copy(selectedSortBy = if (it.selectedSortBy == item) "" else item)
        }
    }

    fun onArtifactTypeChipClicked(artifactType: String) {
        _uiState.update {
            it.copy(
                selectedArtifactTypes = if (artifactType in it.selectedArtifactTypes) it.selectedArtifactTypes - artifactType
                else it.selectedArtifactTypes + artifactType
            )
        }
    }

    fun onMaterialChipClicked(material: String) {
        _uiState.update {
            it.copy(
                selectedMaterials = if (material in it.selectedMaterials) it.selectedMaterials - material
                else it.selectedMaterials + material
            )
        }
    }

    fun onTourTypeChipClicked(tourType: String) {
        _uiState.update {
            it.copy(
                selectedTourTypes = if (tourType in it.selectedTourTypes) it.selectedTourTypes - tourType
                else it.selectedTourTypes + tourType
            )
        }
    }

    fun changeDuration(min: Float, max: Float) {
        _uiState.update {
            it.copy(minDuration = min, maxDuration = max)
        }
    }

    fun onResetClicked() {
        _uiState.update {
            it.copy(
                selectedCategory = "",
                selectedTourismTypes = emptyList(),
                selectedLocations = emptyList(),
                selectedRating = "",
                selectedSortBy = "",
                selectedArtifactTypes = emptyList(),
                selectedMaterials = emptyList(),
                selectedTourTypes = emptyList(),
                minDuration = 0f,
                maxDuration = 30f
            )
        }
    }
}