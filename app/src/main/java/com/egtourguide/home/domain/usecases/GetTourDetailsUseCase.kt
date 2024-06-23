package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetTourDetailsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(tourId: String) = repository.getTourDetails(tourId)
}