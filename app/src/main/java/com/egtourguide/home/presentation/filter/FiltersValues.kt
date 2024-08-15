package com.egtourguide.home.presentation.filter

val categories = listOf("Landmarks", "Artifacts")

val ratings = listOf(
    Rate(title = "5 Only", rating = 5),
    Rate(title = "4 & Up", rating = 4),
    Rate(title = "3 & Up", rating = 3),
    Rate(title = "2 & Up", rating = 2),
    Rate(title = "1 & Up", rating = 1)
)

val sortWays = listOf(
    SortWay(title = "Rating: High To Low", number = 1),
    SortWay(title = "Rating: Low To High", number = 2)
)

data class Rate(val title: String, val rating: Int)

data class SortWay(val title: String, val number: Int)