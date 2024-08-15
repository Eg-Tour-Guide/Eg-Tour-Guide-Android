package com.egtourguide.home.presentation.filter

data class FilterScreenState(
    val filterType: FilterType = FilterType.TOUR,
    val selectedCategory: String = "",

    val tourismTypes: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val rating: String = "",
    val sortBy: String = "",
    val artifactTypes: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val tourTypes: List<String> = emptyList(),

    val selectedTourismTypes: List<String> = emptyList(),
    val selectedLocations: List<String> = emptyList(),
    val selectedRating: Int = -1,
    val selectedSortBy: Int = -1,
    val selectedArtifactTypes: List<String> = emptyList(),
    val selectedMaterials: List<String> = emptyList(),
    val selectedTourTypes: List<String> = emptyList(),
    val minDuration: Float = 0f,
    val maxDuration: Float = 30f
) {
    fun hasChanged() = selectedCategory != "" ||
            selectedTourismTypes.isNotEmpty() ||
            selectedLocations.isNotEmpty() ||
            selectedRating != -1 ||
            selectedSortBy != -1 ||
            selectedArtifactTypes.isNotEmpty() ||
            selectedMaterials.isNotEmpty() ||
            selectedTourTypes.isNotEmpty() ||
            minDuration != 0f ||
            maxDuration != 30f
}
