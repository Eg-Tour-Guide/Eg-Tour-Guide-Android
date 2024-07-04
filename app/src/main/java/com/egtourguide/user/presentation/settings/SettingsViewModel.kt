package com.egtourguide.user.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.domain.usecases.SaveInDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys.NOTIFICATIONS_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveInDataStoreUseCase: SaveInDataStoreUseCase,
    private val getFromDataStoreUseCase: GetFromDataStoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

    init {
        getNotificationsState()
    }

    private fun getNotificationsState() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = getFromDataStoreUseCase(key = NOTIFICATIONS_KEY) ?: false
            _uiState.update { it.copy(pushNotifications = state) }
        }
    }

    fun changeNotificationsState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveInDataStoreUseCase(key = NOTIFICATIONS_KEY, value = state)
            _uiState.update { it.copy(pushNotifications = state) }
        }
    }
}