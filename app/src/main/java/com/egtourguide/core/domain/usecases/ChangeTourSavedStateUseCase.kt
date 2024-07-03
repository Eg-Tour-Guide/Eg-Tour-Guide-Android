package com.egtourguide.core.domain.usecases

import com.egtourguide.core.domain.repository.CommonRepository
import javax.inject.Inject

class ChangeTourSavedStateUseCase @Inject constructor(
    private val repository: CommonRepository
) {
    suspend operator fun invoke(tourId: String) = repository.changeTourSavedState(tourId = tourId)
}