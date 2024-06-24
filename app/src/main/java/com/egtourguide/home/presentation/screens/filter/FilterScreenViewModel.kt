package com.egtourguide.home.presentation.screens.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import javax.inject.Inject

class FilterScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FilterScreenState())
    val uiState = _uiState.asStateFlow()
    private var selectedMap = hashMapOf<String, List<String>>()
    var categoryList = mutableListOf<String>()
    var artifactTypeList = mutableListOf<String>()
    var locationList = mutableListOf<String>()
    var ratingList = mutableListOf<String>()
    private var addedRatingList = mutableListOf<String>()
    var materialList = mutableListOf<String>()
    var tourTypeList = mutableListOf<String>()
    var tourismTypeList = mutableListOf<String>()
    var sortByList = mutableListOf<String>()
    private var addedSortByList = mutableListOf<String>()
    private var queryList = mutableListOf<String>()
    private var durationList = mutableListOf<String>()


    init {
        _uiState.update {
            it.copy(
                categoryFilters = listOf("Tours", "Landmarks", "Artifacts"),
                artifactTypeList = listOf(
                    "Pottery",
                    "Sculpture",
                    "Statue",
                    "Jewelry",
                    "Manuscript",
                    "Tool",
                    "Utensil",
                    "Weapon",
                    "Armor",
                    "Textile",
                    "Coin",
                    "Religiou",
                    "Artistic",
                    "Architectural",
                    "Decorative"
                ),
                materialList = listOf(
                    "Stone", "Clay", "Wood", "Papyrus", "Marble", "Gold", "Silver",
                    "Bronze", "Silk", "Leather", "Glass"
                ),
                locationFilters = listOf(
                    "Alexandria",
                    "Aswan",
                    "Assiut",
                    "Beheira",
                    "Beni Suef",
                    "Cairo",
                    "Dakahlia",
                    "Damietta",
                    "Fayoum",
                    "Gharbia",
                    "Giza",
                    "Ismailia",
                    "Kafr el-Sheikh",
                    "Matrouh",
                    "Minya",
                    "Menofia",
                    "New Valley",
                    "North Sinai",
                    "Port Said",
                    "Qualyubia",
                    "Qena",
                    "Red Sea",
                    "Al-Sharqia",
                    "Sohag",
                    "South Sinai",
                    "Suez",
                    "Luxor"
                ),
                sortList = listOf("Rating: High To Low", "Rating: Low To High"),
                tourismTypeFilters = listOf(
                    "Adventure",
                    "Ecotourism",
                    "Cultural",
                    "Medical",
                    "Historical",
                    "Culinary",
                    "Urban",
                    "Beach",
                    "Sports",
                    "Shopping",
                    "Education", "Various"
                ),
                ratingFilters = listOf("5 Only", "4 & Up", "3 & Up", "2 & Up", "1 & Up"),
                tourTypeList = listOf(
                    "Adventure",
                    "Ecotourism",
                    "Cultural",
                    "Medical",
                    "Historical",
                    "Culinary",
                    "Urban",
                    "Beach",
                    "Sports",
                    "Shopping",
                    "Education"
                )
            )
        }
    }

    fun onApplyClick() {
        selectedMap["Category"] = categoryList
        selectedMap["Tourism Type"] = tourismTypeList
        selectedMap["Artifact Type"] = artifactTypeList
        selectedMap["Tour Type"] = tourTypeList
        selectedMap["Duration"] = durationList
        selectedMap["Location"] = locationList
        selectedMap["Rating"] = addedRatingList
        selectedMap["Sort By"] = addedSortByList
        selectedMap["Material"] = materialList
        selectedMap["Query"] = queryList
        Log.d("```TAG```", "onApplyClick: ${selectedMap["Location"]}")
        _uiState.update { it.copy(isSuccess = true, selectedMap = selectedMap) }
    }

    fun onResetClick() {
        categoryList.clear()
        locationList.clear()
        materialList.clear()
        artifactTypeList.clear()
        tourTypeList.clear()
        tourismTypeList.clear()
        durationList.clear()
        ratingList.clear()
        sortByList.clear()
        addedRatingList.clear()
        addedSortByList.clear()
        _uiState.update { it.copy(reset = true) }

    }
    fun clearRest(){
        _uiState.update { it.copy(reset = false) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun tourScreen() {
        _uiState.update { it.copy(isTours = true) }
    }

    fun landmarkScreen() {
        _uiState.update { it.copy(isLandmarks = true) }
    }

    fun artifactScreen() {
        _uiState.update { it.copy(isArtifacts = true) }
    }

    fun changeDuration(startDay: Int, endDay: Int) {
        durationList.clear()
        durationList.add(startDay.toString())
        durationList.add(endDay.toString())
    }

    fun addSelectedCategoryFilter(label: String) {
        categoryList.clear()
        categoryList.add(label)
        _uiState.update { it.copy(category = label) }
        when (label) {
            "Tours" -> {
                _uiState.update {
                    it.copy(
                        isTours = true,
                        isLandmarks = false,
                        isArtifacts = false
                    )
                }
                tourismTypeList.clear()
                artifactTypeList.clear()
                materialList.clear()

            }

            "Landmarks" -> {
                _uiState.update {
                    it.copy(
                        isTours = false,
                        isLandmarks = true,
                        isArtifacts = false
                    )
                }
                artifactTypeList.clear()
                materialList.clear()
                durationList.clear()
                tourTypeList.clear()

            }

            "Artifacts" -> {
                _uiState.update {
                    it.copy(
                        isTours = true,
                        isLandmarks = false,
                        isArtifacts = true
                    )
                }
                durationList.clear()
                tourTypeList.clear()
                tourismTypeList.clear()
            }
        }
    }

    fun saveQuery(query: String) {

        queryList.add(query)
    }

    fun addSelectedLocationFilter(label: String) {
        locationList.add(label)
    }

    fun removeSelectedLocationFilter(label: String) {
        locationList.remove(label)
    }

    fun addSelectedTourTypeFilter(label: String) {
        tourTypeList.add(label)
    }

    fun removeSelectedTourTypeFilter(label: String) {
        tourTypeList.remove(label)
    }

    fun addSelectedMaterialFilter(label: String) {
        materialList.add(label)
    }

    fun removeSelectedMaterialFilter(label: String) {
        materialList.remove(label)
    }

    fun addSelectedArtifactTypeFilter(label: String) {
        artifactTypeList.add(label)
    }

    fun removeSelectedArtifactTypeFilter(label: String) {
        artifactTypeList.remove(label)
    }

    fun addSelectedRatingFilter(label: String) {
        ratingList.clear()
        addedRatingList.clear()
        ratingList.add(label.first().toString())
        addedRatingList.add(label.first().toString())
    }

    fun removeSelectedRatingFilter(label: String) {
        addedRatingList.clear()
        ratingList.clear()
    }

    fun addSelectedSortByFilter(label: String) {
        sortByList.clear()
        addedSortByList.clear()
        sortByList.add(label)
        if (label == "Rating: High To Low") addedSortByList.add("1")
        if (label == "Rating: Low To High") addedSortByList.add("0")

    }

    fun removeSelectedSortByFilter(label: String) {
        sortByList.clear()
        addedSortByList.clear()
    }

    fun addSelectedTourismTypeFilter(label: String) {
        tourismTypeList.add(label)
    }

    fun removeSelectedTourismTypeFilter(label: String) {
        tourismTypeList.remove(label)
    }


}