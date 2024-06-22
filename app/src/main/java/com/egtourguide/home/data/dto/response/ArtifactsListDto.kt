package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.Artifact

data class ArtifactsListDto(
    val artifacs: List<ArtifactDto>,
    val status: String
) {
    data class ArtifactDto(
        val _id: String,
        val image: String,
        val name: String,
        val saved: Boolean,
        val type: String,
        val material: String,
        val museumName: String
    ) {
        fun toDomainAbstractedArtifact(): AbstractedArtifact {
            return AbstractedArtifact(
                id = _id,
                name = name,
                image = image,
                isSaved = saved,
                type = type,
                material = material,
                museumName = museumName
            )
        }
    }
}