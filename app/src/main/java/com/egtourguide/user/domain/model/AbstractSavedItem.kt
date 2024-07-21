package com.egtourguide.user.domain.model

import com.egtourguide.core.presentation.ItemType

data class AbstractSavedItem(
    val id: String,
    val image: String,
    val name: String,
    var isSaved: Boolean = true,
    val savedItemType: ItemType,
    val location: String = "",
    val duration: Int = 0,
    val category: String = "",
    val material: String = "",
    val artifactType: String = "",
    val ratingAverage: Double = 0.0,
    val ratingCount: Int = 0
)
