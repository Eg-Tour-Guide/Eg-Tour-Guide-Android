package com.egtourguide.core.domain.usecases

import com.egtourguide.core.domain.repository.CommonRepository
import javax.inject.Inject

class ChangeLandmarkSavedStateUseCase @Inject constructor(
    private val repository: CommonRepository
) {
    suspend operator fun invoke(placeId: String) =
        repository.changeLandmarkSavedState(placeId = placeId)
}