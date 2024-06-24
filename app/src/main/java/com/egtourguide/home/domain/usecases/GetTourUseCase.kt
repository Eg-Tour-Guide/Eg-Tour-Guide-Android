package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetTourUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(tourId: String) = repository.getTour(tourId = tourId)
}