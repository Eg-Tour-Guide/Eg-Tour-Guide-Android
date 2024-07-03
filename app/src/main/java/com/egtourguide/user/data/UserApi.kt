package com.egtourguide.user.data

import com.egtourguide.user.data.dto.response.SavedItemsDto
import retrofit2.http.GET

interface UserApi {

    @GET("api/v1/favorite/my-fav")
    suspend fun getSavedItems(): SavedItemsDto
}