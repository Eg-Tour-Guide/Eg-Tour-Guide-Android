package com.egtourguide.detailsAndReviews.expanded

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.model.Review

data class ExpandedScreenState(
    val id: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val callIsSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSaveCall: Boolean = false,
    val images: List<String> = emptyList(),
    val title: String = "",
    val reviewsAverage: Double = 0.0,
    val reviewsCount: Int = 0,
    val reviews: List<Review> = emptyList(),
    val tourismTypes: String = "",
    val isSaved: Boolean = false,
    val description: String = "",
    val location: String = "",
    val artifactType: String = "",
    val artifactMaterials: String = "",
    val latitute: Double = 0.0,
    val longitude: Double = 0.0,
    val includedArtifacts: List<AbstractedArtifact> = emptyList(),
    val relatedPlaces: List<Place> = emptyList(),
    val relatedArtifacts: List<AbstractedArtifact> = emptyList(),
    val vrModel: String = "",
    val arModel: String = "",
    val showAddDialog: Boolean = false,
    val tourID: String = "",
    val tourName: String = "",
    val tourImage: String = "",
    val duration: Int = 0,
    val date: String = "",
    val relatedTours: List<AbstractedTour> = emptyList(),
    val showLoadingDialog: Boolean = false,
    val showAddSuccess: Boolean = false
)