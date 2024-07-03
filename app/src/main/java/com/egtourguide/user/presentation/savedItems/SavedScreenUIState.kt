package com.egtourguide.user.presentation.savedItems

import com.egtourguide.user.domain.model.SavedItem

data class SavedScreenUIState(
    val isLoading: Boolean = true,
    val savedList: List<SavedItem> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
