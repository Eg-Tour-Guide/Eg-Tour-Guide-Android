package com.egtourguide.home.presentation.screens.my_tours

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class MyToursViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(MyToursUIState())
    val uiState = _uiState.asStateFlow()
    val filters: HashMap<*, *>? = null

    fun getMyTours() {

    }

    fun onSaveClicked() {

    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }
}