package com.egtourguide.expanded.presentation.screens.expanded

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.home.domain.model.Review

data class ExpandedScreenState(
    // Loading
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val callIsSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSaveError: Boolean = false,
    val isSaveCall: Boolean = false,
    val isNetworkError: Boolean = false,
    val isRefreshing: Boolean = false,

    // All Data
    val id: String = "",
    val vrModel: String = "",
    val arModel: String = "",
    val date: String = "",
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
    val relatedPlaces: List<AbstractedLandmark> = emptyList(),
    val relatedArtifacts: List<AbstractedArtifact> = emptyList(),
    val relatedTours: List<AbstractedTour> = emptyList(),

    // Dialog
    val showAddDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
    val showAddSuccess: Boolean = false,
    val showAddError: Boolean = false,
    val isTourError: Boolean = false,
    val isDurationError: Boolean = false,
    val tourID: String = "",
    val tourName: String = "",
    val tourImage: String = "",
    val duration: Int = 0
)