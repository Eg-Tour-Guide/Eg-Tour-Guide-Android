package com.egtourguide.user.presentation.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onSaveClicked() {
        // TODO: Implement!!
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000L)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }
}