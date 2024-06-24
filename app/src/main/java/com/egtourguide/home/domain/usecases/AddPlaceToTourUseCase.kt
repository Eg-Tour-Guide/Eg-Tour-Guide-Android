package com.egtourguide.home.domain.usecases

import com.egtourguide.home.data.dto.body.AddPlaceBody
import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class AddPlaceToTourUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(tourId: String, placeId: String, time: Int) =
        repository.addPlaceToTour(
            tourId = tourId,
            addPlaceBody = AddPlaceBody(
                placeId = placeId,
                time = time
            )
        )
}