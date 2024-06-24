package com.egtourguide.home.presentation.screens.saved_items

import androidx.lifecycle.ViewModel
import com.egtourguide.home.domain.model.SavedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedScreenUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun getSavedItems(){

    }

    fun onSaveClicked(item: SavedItem) {

    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }
}