package com.egtourguide.home.data

import com.egtourguide.home.data.body.ReviewRequestBody
import com.egtourguide.home.data.dto.ArtifactsListDto
import com.egtourguide.home.data.dto.HomeDto
import com.egtourguide.home.data.dto.LandmarksListDto
import com.egtourguide.home.data.dto.SearchHistoryDto
import com.egtourguide.home.data.dto.SearchResultsDto
import com.egtourguide.home.data.dto.ToursListDto
import com.egtourguide.home.data.dto.SingleArtifactDto
import com.egtourguide.home.data.dto.SingleLandmarkDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {

    @GET("landmark/{landmarkID}")
    suspend fun getLandmark(
        @Path("landmarkID") landmarkID: String
    ): SingleLandmarkDto

    @GET("artifac/{artifactID}")
    suspend fun getArtifact(
        @Path("artifactID") artifactID: String
    ): SingleArtifactDto

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

    @GET("home")
    suspend fun getHome(): HomeDto

    @POST("api/v1/favorite/add-fav-place/{place_id}")
    suspend fun changePlaceSavedState(
        @Path("place_id") placeID: String,
    )

    @GET("landmarks")
    suspend fun getLandmarksList(): LandmarksListDto

    @GET("artifacs")
    suspend fun getArtifactsList(): ArtifactsListDto

    @POST("api/v1/favorite/add-fav-artifacs/{artifact_id}")
    suspend fun changeArtifactSavedState(
        @Path("artifact_id") artifactID: String,
    )

    @GET("api/v1/tours/all-tours")
    suspend fun getToursList(): ToursListDto

    @POST("api/v1/favorite/add-fav-tour/{tour_id}")
    suspend fun changeTourSavedState(
        @Path("tour_id") tourID: String,
    )

    @GET("search")
    suspend fun search(
        @Query("searchQ") query: String
    ): SearchResultsDto

    @GET("search-history")
    suspend fun getSearchHistory(): SearchHistoryDto
}