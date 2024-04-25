package com.egtourguide.home.data

import retrofit2.http.GET

interface HomeApi {

    // TODO: Change these!!
    @GET("")
    suspend fun getLandmark(): String
    @GET("")
    suspend fun getArtifact(): String
}