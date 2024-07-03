package com.egtourguide.expanded.data

import com.egtourguide.expanded.data.dto.body.AddPlaceBody
import com.egtourguide.expanded.data.dto.body.ReviewRequestBody
import com.egtourguide.expanded.data.dto.body.TourDetailsBody
import com.egtourguide.expanded.data.dto.response.SingleArtifactDto
import com.egtourguide.expanded.data.dto.response.SingleEventDto
import com.egtourguide.expanded.data.dto.response.SingleLandmarkDto
import com.egtourguide.expanded.data.dto.response.SingleTourDto
import com.egtourguide.expanded.data.dto.response.TourDetailsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpandedApi {

    @GET("landmark/{landmarkID}")
    suspend fun getLandmark(
        @Path("landmarkID") landmarkID: String
    ): SingleLandmarkDto

    @GET("artifac/{artifactID}")
    suspend fun getArtifact(
        @Path("artifactID") artifactID: String
    ): SingleArtifactDto

    @GET("api/v1/tours/tour/{tourId}")
    suspend fun getTour(
        @Path("tourId") tourId: String
    ): SingleTourDto

    @GET("api/v1/events/get-event/{eventId}")
    suspend fun getEvent(
        @Path("eventId") eventId: String
    ): SingleEventDto

    @PATCH("api/v1/tours/add-places-tour/{tourId}")
    suspend fun addPlaceToTour(
        @Path("tourId") tourId: String,
        @Body place: AddPlaceBody
    )

    @POST("api/v1/reviews/add-Treview/{tourId}")
    suspend fun reviewTour(
        @Path("tourId") tourId: String,
        @Body requestBody: ReviewRequestBody
    )

    @POST("api/v1/reviews/add-Preview/{placeId}")
    suspend fun reviewPlace(
        @Path("placeId") placeId: String,
        @Body requestBody: ReviewRequestBody
    )

    @GET("/api/v1/tours/tour-details/{tourId}")
    suspend fun getTourDetails(
        @Path("tourId") tourId: String
    ): TourDetailsDto

    @PUT("api/v1/tours/edit-tour-details/{tourId}")
    suspend fun updateTourDetails(
        @Path("tourId") tourId: String,
        @Body tourDetails: TourDetailsBody
    ): String // TODO: Change This!!
}