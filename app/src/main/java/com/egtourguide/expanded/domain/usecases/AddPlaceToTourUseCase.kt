package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.data.dto.body.AddPlaceBody
import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class AddPlaceToTourUseCase @Inject constructor(
    private val repository: ExpandedRepository
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