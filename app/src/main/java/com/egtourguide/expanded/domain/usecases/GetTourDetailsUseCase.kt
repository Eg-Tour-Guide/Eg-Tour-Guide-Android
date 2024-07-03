package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class GetTourDetailsUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(tourId: String) = repository.getTourDetails(tourId)
}