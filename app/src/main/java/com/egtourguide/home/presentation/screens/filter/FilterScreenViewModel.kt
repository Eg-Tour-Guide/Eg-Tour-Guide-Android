package com.egtourguide.home.presentation.screens.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FilterScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FilterScreenState())
    val uiState = _uiState.asStateFlow()
    private var selectedMap = hashMapOf<String, List<String>>()
    private var categoryList = mutableListOf<String>()
    private var artifactTypeList = mutableListOf<String>()
    private var locationList = mutableListOf<String>()
    private var ratingList = mutableListOf<String>()
    private var materialList = mutableListOf<String>()
    private var tourTypeList = mutableListOf<String>()
    private var tourismTypeList = mutableListOf<String>()
    private var sortByList = mutableListOf<String>()
    private var queryList = mutableListOf<String>()
    private var durationList = mutableListOf<String>()



    init {
        _uiState.update {
            it.copy(
                categoryFilters = listOf("Tours", "Landmarks", "Artifacts"),
                artifactTypeList = listOf(
                    "Pottery",
                    "Sculptures",
                    "Statues",
                    "Jewelry",
                    "Manuscripts",
                    "Tools",
                    "Utensils",
                    "Weapons",
                    "Armor",
                    "Textiles",
                    "Coins",
                    "Religious",
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
                sortList = listOf("Featured", "Recently Added"),
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
        selectedMap["Rating"] = ratingList
        selectedMap["Sort By"] = sortByList
        selectedMap["Material"] = materialList
        selectedMap["Query"] = queryList
        _uiState.update { it.copy(isSuccess = true) }
    }

    fun onResetClick() {
        _uiState.update { it.copy(reset = true) }
        categoryList.clear()
        locationList.clear()
        materialList.clear()
        artifactTypeList.clear()
        tourTypeList.clear()
        tourismTypeList.clear()
        durationList.clear()
        ratingList.clear()
        sortByList.clear()

    }
    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
    fun tourScreen(){
        _uiState.update { it.copy(isTours = true) }
    }
    fun landmarkScreen(){
        _uiState.update { it.copy(isLandmarks = true) }
    }
    fun artifactScreen(){
        _uiState.update { it.copy(isArtifacts = true) }
    }

    fun changeDuration(startDay: Int,endDay:Int) {
        durationList.clear()
        durationList.add(startDay.toString())
        durationList.add(endDay.toString())
    }

    fun addSelectedCategoryFilter(label: String) {
        categoryList.clear()
        categoryList.add(label)
        when (label) {
            "Tours" -> _uiState.update { it.copy(isTours = true) }
            "Landmarks" -> _uiState.update { it.copy(isLandmarks = true) }
            "Artifacts" -> _uiState.update { it.copy(isArtifacts = true) }
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
        ratingList.add(label.first().toString())
    }

    fun removeSelectedRatingFilter(label: String) {
        ratingList.clear()
    }

    fun addSelectedSortByFilter(label: String) {
        sortByList.clear()
        sortByList.add(label)
    }

    fun removeSelectedSortByFilter(label: String) {
        sortByList.clear()
    }

    fun addSelectedTourismTypeFilter(label: String) {
        tourismTypeList.add(label)
    }

    fun removeSelectedTourismTypeFilter(label: String) {
        tourismTypeList.remove(label)
    }


}