package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.ArtifactsScreenResponse

data class ArtifactsListDto(
    val artifacs: List<ArtifactDto>,
    val status: String,
    val filter: Filter
) {
    fun toArtifactsResponse() = ArtifactsScreenResponse(
        artifacts = artifacs.map { it.toDomainAbstractedArtifact() },
        artifactTypes = filter.type,
        materials = filter.material
    )

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

    data class Filter(
        val type: List<String>,
        val material: List<String>
    )
}