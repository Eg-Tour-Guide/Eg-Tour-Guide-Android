package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.DetectedArtifact

data class ArtifactDetectionDto(
    val artifact: Artifact,
    val message: String,
    val similarity: Double
) {
    data class Artifact(
        val __v: Int,
        val _id: String,
        val bestMatchImage: String,
        val description: String,
        val images: List<String>,
        val material: String,
        val museum: Mudeum,
        val name: String,
        val type: String
    ) {
        data class Mudeum(
            val _id: String,
            val name: String
        )
    }

    fun toDomainDetectedArtifact(): DetectedArtifact {
        return DetectedArtifact(
            id = artifact._id,
            name = artifact.name,
            similarity = similarity.toFloat(),
            message = message
        )
    }
}