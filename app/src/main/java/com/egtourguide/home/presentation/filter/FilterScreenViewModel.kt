package com.egtourguide.home.presentation.filter

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

    fun setTourFilters(tourTypes: List<String>) {
        _uiState.update { it.copy(tourTypes = tourTypes) }
    }

    fun setLandmarkFilters(tourismTypes: List<String>, locations: List<String>) {
        _uiState.update { it.copy(tourismTypes = tourismTypes, locations = locations) }
    }

    fun setArtifactFilters(artifactTypes: List<String>, materials: List<String>) {
        _uiState.update { it.copy(artifactTypes = artifactTypes, materials = materials) }
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

    fun onRatingChipClicked(rating: Int) {
        _uiState.update {
            it.copy(selectedRating = if (it.selectedRating == rating) -1 else rating)
        }
    }

    fun onSortByChipClicked(item: Int) {
        _uiState.update {
            it.copy(selectedSortBy = if (it.selectedSortBy == item) -1 else item)
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
            it.copy(selectedMinDuration = min, selectedMaxDuration = max)
        }
    }

    fun onResetClicked() {
        _uiState.update {
            it.copy(
                appliedCategory = "",
                appliedTourismTypes = emptyList(),
                appliedLocations = emptyList(),
                appliedRating = -1,
                appliedSortBy = -1,
                appliedArtifactTypes = emptyList(),
                appliedMaterials = emptyList(),
                appliedTourTypes = emptyList(),
                appliedMinDuration = 0f,
                appliedMaxDuration = 30f,
                selectedCategory = "",
                selectedTourismTypes = emptyList(),
                selectedLocations = emptyList(),
                selectedRating = -1,
                selectedSortBy = -1,
                selectedArtifactTypes = emptyList(),
                selectedMaterials = emptyList(),
                selectedTourTypes = emptyList(),
                selectedMinDuration = 0f,
                selectedMaxDuration = 30f
            )
        }
    }

    fun onApplyClicked() {
        _uiState.update {
            it.copy(
                appliedCategory = it.selectedCategory,
                appliedTourismTypes = it.selectedTourismTypes,
                appliedLocations = it.selectedLocations,
                appliedRating = it.selectedRating,
                appliedSortBy = it.selectedSortBy,
                appliedArtifactTypes = it.selectedArtifactTypes,
                appliedMaterials = it.selectedMaterials,
                appliedTourTypes = it.selectedTourTypes,
                appliedMinDuration = it.selectedMinDuration,
                appliedMaxDuration = it.selectedMaxDuration
            )
        }
    }

    fun resetSelected() {
        _uiState.update {
            it.copy(
                selectedCategory = it.appliedCategory,
                selectedTourismTypes = it.appliedTourismTypes,
                selectedLocations = it.appliedLocations,
                selectedRating = it.appliedRating,
                selectedSortBy = it.appliedSortBy,
                selectedArtifactTypes = it.appliedArtifactTypes,
                selectedMaterials = it.appliedMaterials,
                selectedTourTypes = it.appliedTourTypes,
                selectedMinDuration = it.appliedMinDuration,
                selectedMaxDuration = it.appliedMaxDuration,
            )
        }
    }
}