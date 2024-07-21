package com.egtourguide.home.data.dto.response

import com.egtourguide.core.presentation.ItemType
import com.egtourguide.home.domain.model.SearchResult

data class SearchResultsDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val artifacs: List<Artifac>,
        val places: List<Place>
    ) {
        data class Artifac(
            val _id: String,
            val image: String,
            val name: String,
            val type: String,
            val material: String,
            val museumName: String,
            val saved: Boolean
        ) {
            fun toSearchResult(): SearchResult {
                return SearchResult(
                    id = _id,
                    image = image,
                    name = name,
                    location = museumName,
                    isSaved = saved,
                    material = material,
                    artifactType = type,
                    itemType = ItemType.ARTIFACT
                )
            }
        }

        data class Place(
            val _id: String,
            val govName: String,
            val image: String,
            val category: String,
            val name: String,
            val ratingAverage: Double,
            val ratingQuantity: Int,
            val saved: Boolean
        ) {
            fun toSearchResult(): SearchResult {
                return SearchResult(
                    id = _id,
                    image = image,
                    name = name,
                    location = govName,
                    isSaved = saved,
                    category = category,
                    itemType = ItemType.LANDMARK,
                    rating = ratingAverage,
                    ratingCount = ratingQuantity
                )
            }
        }

        fun toDomainSearchResults() =
            artifacs.map { it.toSearchResult() } + places.map { it.toSearchResult() }
    }
}