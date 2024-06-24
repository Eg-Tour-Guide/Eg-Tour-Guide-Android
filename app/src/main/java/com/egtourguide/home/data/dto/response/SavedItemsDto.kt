package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.SavedItem

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
        fun toDomainSavedItem(): SavedItem {
            return SavedItem(
                id = _id,
                image = image,
                name = name,
                artifactType = type,
                location = museumName,
                isArtifact = true
            )
        }
    }

    data class Place(
        val _id: String,
        val category: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int
    ) {
        fun toDomainSavedItem(): SavedItem {
            return SavedItem(
                id = _id,
                image = image,
                name = name,
                location = govName,
                category = category,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity,
                isArtifact = false
            )
        }
    }

    data class Tour(
        val _id: String,
        val image: String,
        val name: String,
        val duration:Int,
        val ratingAverage: Int,
        val ratingQuantity: Int
    ) {
        fun toDomainSavedItem(): SavedItem {
            return SavedItem(
                id = _id,
                image = image,
                name = name,
                duration = duration,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity,
                isTour = true
            )
        }
    }

    fun toDomainSavedItems(): List<SavedItem> {
        return places.map { it.toDomainSavedItem() } + artifacs.map { it.toDomainSavedItem() } + tours.map { it.toDomainSavedItem() }
    }
}