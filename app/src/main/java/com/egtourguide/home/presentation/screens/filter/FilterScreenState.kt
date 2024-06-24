package com.egtourguide.home.presentation.screens.filter

data class FilterScreenState(
    val selectedMap: HashMap<String,List<String>>?=null,
    var categoryFilters: List<String>?=null,
    var locationFilters: List<String>?=null,
    var ratingFilters: List<String>?= null,
    var tourismTypeFilters: List<String>?=null,
    var artifactTypeList: List<String>?= null,
    var tourTypeList: List<String>?= null,
    var materialList: List<String>?= null,
    var sortList: List<String>?= null,
    val duration: List<String>?=null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = "",
    val isLandmarks: Boolean=false,
    val isArtifacts: Boolean=false,
    val isTours: Boolean=false,
    val reset:Boolean=false,
    val category:String=" "
)

