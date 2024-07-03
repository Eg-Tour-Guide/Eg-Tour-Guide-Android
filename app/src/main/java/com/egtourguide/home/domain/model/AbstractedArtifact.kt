package com.egtourguide.home.domain.model

data class AbstractedArtifact(
    val id: String,
    val name: String,
    val image: String,
    var isSaved: Boolean,
    val type: String,
    val material: String,
    val museumName: String
)
