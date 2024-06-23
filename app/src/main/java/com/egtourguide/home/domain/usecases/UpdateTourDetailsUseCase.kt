package com.egtourguide.home.domain.usecases

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.home.data.dto.body.TourDetailsBody
import com.egtourguide.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTourDetailsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        tourId: String,
        name: String? = null,
        description: String? = null,
        startDate: String? = null
    ): Flow<ResultWrapper<String>> {
        val tourDetails = TourDetailsBody(
            name = name,
            description = description,
            startDate = startDate
        )

        return repository.updateTourDetails(tourId = tourId, tourDetails = tourDetails)
    }
}