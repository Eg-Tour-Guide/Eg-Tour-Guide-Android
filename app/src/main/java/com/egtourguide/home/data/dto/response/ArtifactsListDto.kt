package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedArtifact

data class ArtifactsListDto(
    val artifacs: List<ArtifactDto>,
    val status: String
) {
    data class ArtifactDto(
        val _id: String,
        val image: String,
        val name: String,
        val saved: Boolean,
        val museumName: String,
        val type: String?,
        val material: String?
    ) {
        fun toDomainAbstractedArtifact(): AbstractedArtifact {
            return AbstractedArtifact(
                id = _id,
                name = name,
                image = image,
                isSaved = saved,
                museumName = museumName,
                type = type ?: "",
                material = material ?: ""
            )
        }
    }
}