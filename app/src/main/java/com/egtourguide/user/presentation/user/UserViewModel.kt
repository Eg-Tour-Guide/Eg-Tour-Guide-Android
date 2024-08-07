package com.egtourguide.user.presentation.user

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.usecases.DeleteFromDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys.IS_LOGGED_KEY
import com.egtourguide.core.utils.DataStoreKeys.TOKEN_KEY
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
    private val deleteFromDataStoreUseCase: DeleteFromDataStoreUseCase
) : ViewModel() {

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
                onFailure = { error ->
                    _uiState.update { it.copy(isDetectionLoading = false, error = error) }
                }
            )
        }
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromDataStoreUseCase(key = IS_LOGGED_KEY)
            deleteFromDataStoreUseCase(key = TOKEN_KEY)
        }
    }
}