package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class GetLandmarkUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(id: String) = repository.getLandmark(id = id)
}