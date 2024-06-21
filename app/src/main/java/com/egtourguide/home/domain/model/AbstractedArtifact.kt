package com.egtourguide.home.domain.model

data class AbstractedArtifact(
    val id: String,
    val name: String,
    val image: String,
    var isSaved: Boolean,
    val museumName: String,
    val type: String,
    val material: String
)
