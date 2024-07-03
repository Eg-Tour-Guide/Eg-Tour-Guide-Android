package com.egtourguide.customTours.data

import com.egtourguide.user.data.dto.response.MyToursDto
import retrofit2.http.GET

interface CustomToursApi {

    @GET("/api/v1/tours/user-Tours")
    suspend fun getMyTours(): MyToursDto
}