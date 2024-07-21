package com.egtourguide.user.data.dto.response

import com.egtourguide.core.presentation.ItemType
import com.egtourguide.user.domain.model.AbstractSavedItem

data class SavedItemsDto(
    val artifacs: List<Artifac>,
    val places: List<Place>,
    val status: String,
    val tours: List<Tour>
) {
    data class Artifac(
        val _id: String,
        val image: String,
        val museumName: String,
        val name: String,
        val type: String
    ) {
        fun toDomainSavedItem(): AbstractSavedItem {
            return AbstractSavedItem(
                id = _id,
                image = image,
                name = name,
                artifactType = type,
                location = museumName,
                savedItemType = ItemType.ARTIFACT
            )
        }
    }

    data class Place(
        val _id: String,
        val category: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Double,
        val ratingQuantity: Int
    ) {
        fun toDomainSavedItem(): AbstractSavedItem {
            return AbstractSavedItem(
                id = _id,
                image = image,
                name = name,
                location = govName,
                category = category,
                ratingAverage = ratingAverage,
                ratingCount = ratingQuantity,
                savedItemType = ItemType.LANDMARK
            )
        }
    }

    data class Tour(
        val _id: String,
        val image: String,
        val name: String,
        val duration: Int,
        val ratingAverage: Double,
        val ratingQuantity: Int
    ) {
        fun toDomainSavedItem(): AbstractSavedItem {
            return AbstractSavedItem(
                id = _id,
                image = image,
                name = name,
                duration = duration,
                ratingAverage = ratingAverage,
                ratingCount = ratingQuantity,
                savedItemType = ItemType.TOUR
            )
        }
    }

    fun toDomainSavedItems(): List<AbstractSavedItem> {
        return places.map { it.toDomainSavedItem() } +
                artifacs.map { it.toDomainSavedItem() } +
                tours.map { it.toDomainSavedItem() }
    }
}