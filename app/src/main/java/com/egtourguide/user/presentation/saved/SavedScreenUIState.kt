package com.egtourguide.user.presentation.saved

import com.egtourguide.user.domain.model.AbstractSavedItem

data class SavedScreenUIState(
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isCallSent: Boolean = false,
    val savedList: List<AbstractSavedItem> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null
)
