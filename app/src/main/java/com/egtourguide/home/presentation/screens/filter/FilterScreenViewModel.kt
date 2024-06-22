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
    private var selectedMap= hashMapOf<String,List<String>>()

    fun changeDuration(days:Int){
        _uiState.update { it.copy(duration = days) }
    }
    fun addSelectedFilters(label:String,filter:String){
        selectedMap.put(label,filter)
        _uiState.update { it.copy(selected =selectedList ) }
    }
    fun removeSelectedFilter(filter: String){
        selectedList.remove(filter)
        _uiState.update { it.copy(selected =selectedList ) }
    }

}