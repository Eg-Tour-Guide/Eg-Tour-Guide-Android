package com.egtourguide.home.data

import com.egtourguide.home.data.body.ReviewRequestBody
import com.egtourguide.home.data.dto.response.ArtifactDetectionDto
import com.egtourguide.home.data.dto.response.ArtifactsListDto
import retrofit2.http.Body
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import com.egtourguide.home.data.dto.response.HomeDto
import com.egtourguide.home.data.dto.response.LandmarksListDto
import com.egtourguide.home.data.dto.response.SearchHistoryDto
import com.egtourguide.home.data.dto.response.SearchResultsDto
import com.egtourguide.home.data.dto.response.ToursListDto
import com.egtourguide.home.data.dto.response.SingleArtifactDto
import com.egtourguide.home.data.dto.response.SingleLandmarkDto
import com.egtourguide.home.data.dto.response.TourDetailsDto
import com.egtourguide.home.data.dto.body.TourDetailsBody
import com.egtourguide.home.data.dto.response.MyToursDto
import com.egtourguide.home.data.dto.response.SavedItemsDto
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @DELETE("delete-search-history")
    suspend fun deleteSearchHistory()

    @Multipart
    @POST("rec")
    suspend fun detectArtifact(
        @Part photo: MultipartBody.Part
    ): ArtifactDetectionDto

    @GET("/api/v1/tours/tour-details/{tourId}")
    suspend fun getTourDetails(
        @Path("tourId") tourId: String
    ): TourDetailsDto

    @PUT("api/v1/tours/edit-tour-details/{tourId}")
    suspend fun updateTourDetails(
        @Path("tourId") tourId: String,
        @Body tourDetails: TourDetailsBody
    ): String // TODO: Change This!!

    @GET("api/v1/favorite/my-fav")
    suspend fun getSavedItems(): SavedItemsDto

    @GET("/api/v1/tours/user-Tours")
    suspend fun getMyTours(): MyToursDto
}