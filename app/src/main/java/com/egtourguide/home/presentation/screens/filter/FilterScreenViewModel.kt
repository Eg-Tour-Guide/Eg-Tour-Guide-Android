package com.egtourguide.home.presentation.screens.filter

import androidx.lifecycle.ViewModel
import com.egtourguide.home.presentation.screens.home.HomeUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FilterScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FilterScreenState())
    val uiState = _uiState.asStateFlow()
    private var selectedList= mutableListOf<String>()
    fun addSelectedFilters(filter:String){
        selectedList.add(filter)
        _uiState.update { it.copy(selected =selectedList ) }
    }
    fun removeSelectedFilter(filter: String){
        selectedList.remove(filter)
    }

}