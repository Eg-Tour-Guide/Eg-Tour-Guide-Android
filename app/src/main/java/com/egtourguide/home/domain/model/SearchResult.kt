package com.egtourguide.home.domain.model

import com.egtourguide.core.presentation.ItemType

data class SearchResult(
    val id: String,
    val image: String,
    val name: String,
    val location: String,
    var isSaved: Boolean,
    val itemType: ItemType,
    val category: String = "",
    val material: String = "",
    val artifactType: String = "",
    val rating: Double = 0.0,
    val ratingCount: Int = 0
)
