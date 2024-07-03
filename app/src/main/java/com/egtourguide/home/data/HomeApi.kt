package com.egtourguide.home.data

import com.egtourguide.home.data.dto.response.ArtifactDetectionDto
import com.egtourguide.home.data.dto.response.ArtifactsListDto
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import com.egtourguide.home.data.dto.response.HomeDto
import com.egtourguide.home.data.dto.response.LandmarksListDto
import com.egtourguide.home.data.dto.response.SearchHistoryDto
import com.egtourguide.home.data.dto.response.SearchResultsDto
import com.egtourguide.home.data.dto.response.ToursListDto
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface HomeApi {

    @GET("home")
    suspend fun getHome(): HomeDto

    @GET("api/v1/tours/all-tours")
    suspend fun getToursList(): ToursListDto

    @GET("landmarks")
    suspend fun getLandmarksList(): LandmarksListDto

    @GET("artifacs")
    suspend fun getArtifactsList(): ArtifactsListDto

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
}