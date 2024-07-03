package com.egtourguide.customTours.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.customTours.data.CustomToursApi
import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class CustomToursRepositoryImpl @Inject constructor(
    private val customToursApi: CustomToursApi
) : CustomToursRepository {

    override suspend fun getMyTours() = safeCall {
        customToursApi.getMyTours().tours.map { it.toDomainAbstractedTour() }
    }
}