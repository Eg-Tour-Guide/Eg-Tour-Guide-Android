package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.Artifact

data class ArtifactDto(
    val id: String
) {
    fun toArtifact() = Artifact(
        id = id
    )
}
