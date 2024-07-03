package com.egtourguide.user.presentation.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

    fun changeNotificationsState(state: Boolean) {
        _uiState.update { it.copy(pushNotifications = state) }
    }
}