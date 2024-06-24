package com.egtourguide.home.data

import com.egtourguide.home.data.dto.ArtifactDetectionDto
import com.egtourguide.home.data.dto.HomeDto
import com.egtourguide.home.data.dto.LandmarksListDto
import com.egtourguide.home.data.dto.SavedItemsDto
import com.egtourguide.home.data.dto.SearchHistoryDto
import com.egtourguide.home.data.dto.SearchResultsDto
import com.egtourguide.home.data.dto.ToursListDto
import com.egtourguide.home.data.dto.SingleArtifactDto
import com.egtourguide.home.data.dto.SingleLandmarkDto
import com.egtourguide.home.data.dto.response.ArtifactsListDto
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
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

    @GET("api/v1/favorite/my-fav")
    suspend fun getSavedItems():SavedItemsDto
}