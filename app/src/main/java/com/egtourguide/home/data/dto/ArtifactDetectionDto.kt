package com.egtourguide.home.data.dto

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
        val museum: String,
        val name: String,
        val type: String
    )

    fun toDomainDetectedArtifact(): DetectedArtifact {
        return DetectedArtifact(
            id = artifact._id,
            name = artifact.name,
            similarity = similarity.toFloat(),
            message = message
        )
    }
}