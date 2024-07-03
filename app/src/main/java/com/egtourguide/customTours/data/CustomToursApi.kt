package com.egtourguide.customTours.data

import com.egtourguide.customTours.data.dto.body.CreateTourBody
import com.egtourguide.customTours.data.dto.body.EditTourBody
import com.egtourguide.customTours.data.dto.response.MyToursDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomToursApi {

    @GET("/api/v1/tours/user-Tours")
    suspend fun getMyTours(): MyToursDto

    @POST("/api/v1/tours/create-tour")
    suspend fun createTour(
        @Body body: CreateTourBody
    )

    @PUT("/api/v1/tours/edit-tour-details/{tourId}")
    suspend fun editTour(
        @Path("tourId") tourId: String,
        @Body body: EditTourBody
    )
}