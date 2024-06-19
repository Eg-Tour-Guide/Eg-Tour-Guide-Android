package com.egtourguide.home.data

import com.egtourguide.home.data.dto.response.ArtifactsListDto
import com.egtourguide.home.data.dto.response.HomeDto
import com.egtourguide.home.data.dto.response.LandmarksListDto
import com.egtourguide.home.data.dto.response.SingleArtifactDto
import com.egtourguide.home.data.dto.response.SingleLandmarkDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    suspend fun getLandmarksList():LandmarksListDto

    @GET("artifacs")
    suspend fun getArtifactsList():ArtifactsListDto

    @POST("api/v1/favorite/add-fav-artifacs/{artifact_id}")
    suspend fun changeArtifactSavedState(
        @Path("artifact_id") artifactID: String,
    )
}