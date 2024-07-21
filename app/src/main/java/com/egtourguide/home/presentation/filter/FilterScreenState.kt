package com.egtourguide.home.presentation.filter

data class FilterScreenState(
    val filterType: FilterType = FilterType.TOUR,
    val selectedCategory: String = "",
    val selectedTourismTypes: List<String> = emptyList(),
    val selectedLocations: List<String> = emptyList(),
    val selectedRating: String = "",
    val selectedSortBy: String = "",
    val selectedArtifactTypes: List<String> = emptyList(),
    val selectedMaterials: List<String> = emptyList(),
    val selectedTourTypes: List<String> = emptyList(),
    val minDuration: Float = 0f,
    val maxDuration: Float = 30f
) {
    fun hasChanged() = selectedCategory != "" ||
            selectedTourismTypes.isNotEmpty() ||
            selectedLocations.isNotEmpty() ||
            selectedRating != "" ||
            selectedSortBy != "" ||
            selectedArtifactTypes.isNotEmpty() ||
            selectedMaterials.isNotEmpty() ||
            selectedTourTypes.isNotEmpty() ||
            minDuration != 0f ||
            maxDuration != 30f
}
