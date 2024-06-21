package com.egtourguide.home.data.dto.response

data class SearchHistoryDto(
    val search: List<Search>,
    val status: String
) {
    data class Search(
        val _id: String,
        val search: String
    )
}