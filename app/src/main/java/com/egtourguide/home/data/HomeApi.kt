package com.egtourguide.home.data

import com.egtourguide.home.data.dto.response.ArtifactsListDto
import com.egtourguide.home.data.dto.response.HomeDto
import com.egtourguide.home.data.dto.response.LandmarksListDto
import com.egtourguide.home.data.dto.response.SearchHistoryDto
import com.egtourguide.home.data.dto.response.SearchResultsDto
import com.egtourguide.home.data.dto.response.ToursListDto
import com.egtourguide.home.data.dto.response.SingleArtifactDto
import com.egtourguide.home.data.dto.response.SingleLandmarkDto
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