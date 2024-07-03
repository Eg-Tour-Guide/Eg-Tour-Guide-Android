package com.egtourguide.customTours.domain.usecases

import com.egtourguide.customTours.data.dto.body.CreateTourBody
import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class CreateTourUseCase @Inject constructor(
    private val repository: CustomToursRepository
) {
    suspend operator fun invoke(name: String, description: String) = repository.createTour(
        CreateTourBody(
            name = name, description = description
        )
    )
}