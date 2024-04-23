package com.egtourguide.home.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedScreenState())
    val uiState = _uiState.asStateFlow()

    fun changeSavedState() {
        _uiState.update { it.copy(isSaved = !it.isSaved) }
    }
}