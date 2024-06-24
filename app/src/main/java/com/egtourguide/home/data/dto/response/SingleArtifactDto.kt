package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.Artifact

data class SingleArtifactDto(
    val status: String,
    val artifac: ArtifactDto,
    val relatedArtifacs: List<RelatedArtifact>?
) {
    fun toArtifact() = Artifact(
        id = artifac._id,
        title = artifac.name,
        images = artifac.images,
        description = artifac.description,
        museum = artifac.museum.name,
        type = artifac.type,
        material = artifac.material,
        saved = artifac.saved,
        relatedArtifacts = relatedArtifacs?.map { it.toAbstractedArtifact() } ?: emptyList(),
        arModel = artifac.ar ?: ""
    )
}

data class ArtifactDto(
    val _id: String,
    val name: String,
    val museum: MuseumDto,
    val images: List<String>,
    val description: String,
    val type: String,
    val material: String,
    val saved: Boolean,
    val ar: String?
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
    val museumName: String,
    val type: String,
    val material: String,
    val saved: Boolean
) {
    fun toAbstractedArtifact() = AbstractedArtifact(
        id = _id,
        name = name,
        image = image,
        isSaved = saved,
        museumName = museumName,
        type = type,
        material = material
    )
}