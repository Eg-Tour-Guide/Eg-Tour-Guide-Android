package com.egtourguide.user.presentation.user

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.usecases.DeleteFromDataStoreUseCase
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys.IS_LOGGED_KEY
import com.egtourguide.core.utils.DataStoreKeys.TOKEN_KEY
import com.egtourguide.core.utils.DataStoreKeys.USER_NAME_KEY
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val detectArtifactUseCase: DetectArtifactUseCase,
    private val deleteFromDataStoreUseCase: DeleteFromDataStoreUseCase,
    private val getFromDataStoreUseCase: GetFromDataStoreUseCase
) : ViewModel() {

    init {
        getUsername()
    }

    private val _uiState = MutableStateFlow(UserScreenState())
    val uiState = _uiState.asStateFlow()

    fun detectArtifact(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            detectArtifactUseCase(bitmap = image).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isDetectionLoading = true, detectedArtifact = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isDetectionLoading = false,
                            detectedArtifact = response
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isDetectionLoading = false, isDetectionError = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isDetectionLoading = false, isDetectionError = true) }
                }
            )
        }
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }

    fun clearDetectionError() {
        _uiState.update { it.copy(isDetectionError = false) }
    }

    private fun getUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val username = getFromDataStoreUseCase(key = USER_NAME_KEY)
            username?.let { it2 ->
                _uiState.update { it.copy(username = it2) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromDataStoreUseCase(key = IS_LOGGED_KEY)
            deleteFromDataStoreUseCase(key = TOKEN_KEY)
        }
    }
}