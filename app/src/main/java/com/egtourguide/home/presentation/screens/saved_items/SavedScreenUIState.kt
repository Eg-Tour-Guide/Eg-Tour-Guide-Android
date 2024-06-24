package com.egtourguide.home.presentation.screens.saved_items

import com.egtourguide.home.domain.model.SavedItem

data class SavedScreenUIState(
    val isLoading: Boolean = true,
    val savedList: List<SavedItem> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
