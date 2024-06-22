package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class ChangeTourSavedStateUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(tourId: String) = homeRepository.changeTourSavedState(
        tourId = tourId
    )
}