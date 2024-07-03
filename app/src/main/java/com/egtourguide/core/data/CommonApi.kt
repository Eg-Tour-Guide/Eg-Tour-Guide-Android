package com.egtourguide.core.data

import retrofit2.http.POST
import retrofit2.http.Path

interface CommonApi {

    @POST("api/v1/favorite/add-fav-place/{place_id}")
    suspend fun changePlaceSavedState(
        @Path("place_id") placeID: String,
    )

    @POST("api/v1/favorite/add-fav-artifacs/{artifact_id}")
    suspend fun changeArtifactSavedState(
        @Path("artifact_id") artifactID: String,
    )

    @POST("api/v1/favorite/add-fav-tour/{tour_id}")
    suspend fun changeTourSavedState(
        @Path("tour_id") tourID: String,
    )
}