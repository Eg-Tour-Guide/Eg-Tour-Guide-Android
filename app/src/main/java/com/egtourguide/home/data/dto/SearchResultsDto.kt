package com.egtourguide.home.data.dto

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
            val saved: Boolean
        ) {
            fun toSearchResult(): SearchResult {
                return SearchResult(
                    id = _id,
                    image = image,
                    name = name,
                    location = "",
                    isSaved = saved,
                    isLandmark = false,
                    rating = null,
                    ratingCount = null
                )
            }
        }

        data class Place(
            val _id: String,
            val govName: String,
            val image: String,
            val name: String,
            val ratingAverage: Int,
            val ratingQuantity: Int,
            val saved: Boolean
        ) {
            fun toSearchResult(): SearchResult {
                return SearchResult(
                    id = _id,
                    image = image,
                    name = name,
                    location = "",
                    isSaved = saved,
                    isLandmark = false,
                    rating = null,
                    ratingCount = null
                )
            }
        }

        fun toDomainSearchResults(): List<SearchResult> {
            val results = artifacs.map { it.toSearchResult() } + places.map { it.toSearchResult() }
            return results
        }
    }
}