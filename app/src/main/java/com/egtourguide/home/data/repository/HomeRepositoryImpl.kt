package com.egtourguide.home.data.repository

import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {
}