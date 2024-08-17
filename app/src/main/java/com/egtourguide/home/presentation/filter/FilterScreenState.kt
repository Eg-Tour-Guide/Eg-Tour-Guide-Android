package com.egtourguide.home.presentation.filter

data class FilterScreenState(
    val filterType: FilterType = FilterType.TOUR,

    // Shown data
    val tourismTypes: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val rating: String = "",
    val artifactTypes: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val tourTypes: List<String> = emptyList(),

    // Currently selected but not applied
    val selectedCategory: String = "",
    val selectedTourismTypes: List<String> = emptyList(),
    val selectedLocations: List<String> = emptyList(),
    val selectedRating: Int = -1,
    val selectedSortBy: Int = -1,
    val selectedArtifactTypes: List<String> = emptyList(),
    val selectedMaterials: List<String> = emptyList(),
    val selectedTourTypes: List<String> = emptyList(),
    val selectedMinDuration: Float = 0f,
    val selectedMaxDuration: Float = 30f,

    // Applied filters
    val appliedCategory: String = "",
    val appliedTourismTypes: List<String> = emptyList(),
    val appliedLocations: List<String> = emptyList(),
    val appliedRating: Int = -1,
    val appliedSortBy: Int = -1,
    val appliedArtifactTypes: List<String> = emptyList(),
    val appliedMaterials: List<String> = emptyList(),
    val appliedTourTypes: List<String> = emptyList(),
    val appliedMinDuration: Float = 0f,
    val appliedMaxDuration: Float = 30f
) {
    fun hasChanged() = appliedCategory != "" ||
            appliedTourismTypes.isNotEmpty() ||
            appliedLocations.isNotEmpty() ||
            appliedRating != -1 ||
            appliedSortBy != -1 ||
            appliedArtifactTypes.isNotEmpty() ||
            appliedMaterials.isNotEmpty() ||
            appliedTourTypes.isNotEmpty() ||
            appliedMinDuration != 0f ||
            appliedMaxDuration != 30f
}
