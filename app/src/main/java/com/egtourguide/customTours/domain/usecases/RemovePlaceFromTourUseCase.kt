package com.egtourguide.customTours.domain.usecases

import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class RemovePlaceFromTourUseCase @Inject constructor(
    private val customToursRepository: CustomToursRepository
) {
    suspend operator fun invoke(tourId: String, placeId: String) =
        customToursRepository.removePlaceFromTour(
            tourId = tourId,
            placeId = placeId
        )
}