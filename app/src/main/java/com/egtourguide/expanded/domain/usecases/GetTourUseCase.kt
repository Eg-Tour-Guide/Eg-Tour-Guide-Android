package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class GetTourUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(tourId: String) = repository.getTour(tourId = tourId)
}