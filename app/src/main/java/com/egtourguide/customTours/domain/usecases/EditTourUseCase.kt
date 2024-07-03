package com.egtourguide.customTours.domain.usecases

import com.egtourguide.customTours.data.dto.body.EditTourBody
import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class EditTourUseCase @Inject constructor(
    private val repository: CustomToursRepository
) {
    suspend operator fun invoke(tourId: String, name: String, description: String) =
        repository.editTour(
            tourId = tourId,
            body = EditTourBody(
                name = name,
                description = description
            )
        )
}