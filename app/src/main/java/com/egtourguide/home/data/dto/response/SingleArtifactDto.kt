package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.Artifact
import com.egtourguide.home.domain.model.Place

data class SingleArtifactDto(
    val status: String,
    val artifac: ArtifactDto,
    val relatedArtifacs: List<RelatedArtifact>
) {
    fun toArtifact() = Artifact(
        id = artifac._id,
        title = artifac.name,
        images = artifac.images,
        description = artifac.description,
        museum = artifac.museum.name,
        saved = artifac.saved,
        relatedArtifacts = relatedArtifacs.map { it.toPlace() },
    )
}

data class ArtifactDto(
    val _id: String,
    val name: String,
    val museum: MuseumDto,
    val images: List<String>,
    val description: String,
    val saved: Boolean
)

data class MuseumDto(
    val _id: String,
    val name: String,
    val id: String
)

data class RelatedArtifact(
    val _id: String,
    val name: String,
    val image: String,
    val saved: Boolean
) {
    fun toPlace() = Place(
        id = _id,
        name = name,
        image = image,
        location = "Test",
        isSaved = saved,
        rating = 4.5f,
        ratingCount = 45
    )
}