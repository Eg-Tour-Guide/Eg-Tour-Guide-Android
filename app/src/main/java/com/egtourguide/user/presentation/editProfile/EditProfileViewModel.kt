package com.egtourguide.user.presentation.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys.USER_EMAIL_KEY
import com.egtourguide.core.utils.DataStoreKeys.USER_NAME_KEY
import com.egtourguide.core.utils.DataStoreKeys.USER_PHONE_KEY
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
    private val getFromDataStoreUseCase: GetFromDataStoreUseCase
) : ViewModel() {

    init {
        getUserData()
    }

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val name = getFromDataStoreUseCase(key = USER_NAME_KEY)
            val email = getFromDataStoreUseCase(key = USER_EMAIL_KEY)
            val phone = getFromDataStoreUseCase(key = USER_PHONE_KEY)

            name?.let { it2 -> _uiState.update { it.copy(name = it2) } }
            email?.let { it2 -> _uiState.update { it.copy(email = it2) } }
            phone?.let { it2 -> _uiState.update { it.copy(phone = it2) } }
        }
    }

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