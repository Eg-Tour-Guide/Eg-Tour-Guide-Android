package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.data.dto.body.AddPlaceBody
import com.egtourguide.home.data.dto.body.ReviewRequestBody
import com.egtourguide.home.data.dto.body.TourDetailsBody
import com.egtourguide.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    override suspend fun getLandmark(id: String) = safeCall {
        homeApi.getLandmark(landmarkID = id).toLandmark()
    }

    override suspend fun getArtifact(id: String) = safeCall {
        homeApi.getArtifact(artifactID = id).toArtifact()
    }

    override suspend fun getHome() = safeCall {
        homeApi.getHome().toDomainHome()
    }

    override suspend fun sendTourReview(
        tourId: String,
        requestBody: ReviewRequestBody
    ) = safeCall {
        homeApi.reviewTour(tourId, requestBody)
    }


    override suspend fun sendPlaceReview(
        placeId: String,
        requestBody: ReviewRequestBody
    ) = safeCall {
        homeApi.reviewPlace(placeId, requestBody)
    }

    override suspend fun changePlaceSavedState(placeId: String) = safeCall {
        homeApi.changePlaceSavedState(placeId)
    }

    override suspend fun getLandmarksList() = safeCall {
        homeApi.getLandmarksList().places.map { it.toDomainPlace() }
    }

    override suspend fun getArtifactsList() = safeCall {
        homeApi.getArtifactsList().artifacs.map { it.toDomainAbstractedArtifact() }
    }

    override suspend fun changeArtifactSavedState(artifactId: String) = safeCall {
        homeApi.changeArtifactSavedState(artifactId)
    }

    override suspend fun getToursList() = safeCall {
        homeApi.getToursList().tours.map { it.toDomainAbstractedTour() }
    }

    override suspend fun changeTourSavedState(tourId: String) = safeCall {
        homeApi.changeTourSavedState(tourId)
    }

    override suspend fun search(query: String) = safeCall {
        homeApi.search(query).data.toDomainSearchResults()
    }

    override suspend fun getSearchHistory() = safeCall {
        homeApi.getSearchHistory().search.map { it.search }
    }

    override suspend fun deleteSearchHistory() = safeCall {
        homeApi.deleteSearchHistory()
    }

    override suspend fun detectArtifact(image: MultipartBody.Part) = safeCall {
        homeApi.detectArtifact(photo = image).toDomainDetectedArtifact()
    }

    override suspend fun getTourDetails(tourId: String) = safeCall {
        homeApi.getTourDetails(tourId).toTourDetails()
    }

    override suspend fun updateTourDetails(
        tourId: String,
        tourDetails: TourDetailsBody
    ) = safeCall {
        homeApi.updateTourDetails(tourId = tourId, tourDetails = tourDetails)
    }

    override suspend fun getTour(tourId: String) = safeCall {
        homeApi.getTour(tourId = tourId).toTour()
    }

    override suspend fun getEvent(eventId: String) = safeCall {
        homeApi.getEvent(eventId).toSingleEvent()
    }

    override suspend fun addPlaceToTour(
        tourId: String,
        addPlaceBody: AddPlaceBody
    ) = safeCall {
        homeApi.addPlaceToTour(tourId = tourId, place = addPlaceBody)
    }
}