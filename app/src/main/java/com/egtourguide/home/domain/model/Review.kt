package com.egtourguide.home.domain.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Review(
    val id: String,
    val authorName: String,
    val authorImage: String,
    val rating: Int,
    val description: String
)

fun List<Review>.toStringJson(): String = Gson().toJson(
    this,
    object : TypeToken<ArrayList<Review>>() {}.type
)

fun String.toReviewsList(): List<Review> = Gson().fromJson(
    this,
    object : TypeToken<ArrayList<Review>>() {}.type
)