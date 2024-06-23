package com.egtourguide.home.presentation.myTours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.GetToursListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Change all the logic!!!!
@HiltViewModel
class MyToursListViewModel @Inject constructor(
    private val getToursListUseCase: GetToursListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyToursListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getToursList()
    }

    private fun getToursList() {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, tours = response) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }
}