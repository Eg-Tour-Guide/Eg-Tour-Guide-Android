package com.egtourguide.home.presentation.screens.filter

data class FilterScreenState(
    val selected: List<String>?=null,
    var categoryFilters: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var locationFilters: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var ratingFilters: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var tourismTypeFilters: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var artifactTypeList: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var materialList: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    var sortList: List<Filter>?= listOf(Filter("Test1",false),Filter("Test2",true)),
    val duration: Int=0
)
data class Filter(
    val label: String = " ",
    var isSelected: Boolean = false
)
