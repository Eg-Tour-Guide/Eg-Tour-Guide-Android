package com.egtourguide.user.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.user.domain.model.AbstractSavedItem
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.core.utils.ItemType
import com.egtourguide.user.domain.usecases.GetSavedItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getSavedItemsUseCase: GetSavedItemsUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun getSavedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedItemsUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = null, isCallSent = true) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, savedList = response) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    // TODO: Check this logic!!
    fun onSaveClicked(item: AbstractSavedItem) {
        viewModelScope.launch(Dispatchers.IO) {
            item.isSaved = !item.isSaved

            when (item.itemType) {
                ItemType.ARTIFACT -> {
                    changeArtifactSavedStateUseCase(
                        artifactId = item.id
                    ).onResponse(
                        onLoading = {},
                        onSuccess = {
                            _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(saveError = error) }
                        },
                        onNetworkError = {
                            // TODO: Show save error!!
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    )
                }

                ItemType.TOUR -> {
                    changeTourSavedStateUseCase(item.id).onResponse(
                        onLoading = {},
                        onSuccess = {
                            _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(saveError = error) }
                        },
                        onNetworkError = {
                            // TODO: Show save error!!
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    )
                }

                ItemType.LANDMARK -> {
                    changeLandmarkSavedStateUseCase(item.id).onResponse(
                        onLoading = {},
                        onSuccess = {
                            _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(saveError = error) }
                        },
                        onNetworkError = {
                            // TODO: Show save error!!
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    )
                }
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    fun filterSavedItems() {
        // TODO: Implement this!!
    }
}