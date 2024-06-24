package com.egtourguide.home.data.dto

data class SavedItemsDto(
    val favorites: List<Favorite>,
    val status: String
) {
    data class Favorite(
        val __v: Int,
        val _id: String,
        val artifacs: Artifacs,
        val place: Place,
        val user: String
    ) {
        data class Artifacs(
            val __v: Int,
            val _id: String,
            val description: String,
            val id: String,
            val images: List<String>,
            val material: String,
            val museum: String,
            val name: String,
            val type: String
        )

        data class Place(
            val __v: Int,
            val _id: String,
            val category: String,
            val description: String,
            val govName: String,
            val id: String,
            val images: List<String>,
            val location: Location,
            val name: String,
            val ratingAverage: Int,
            val ratingQuantity: Int,
            val type: String
        ) {
            data class Location(
                val coordinates: List<Double>
            )
        }
    }
}