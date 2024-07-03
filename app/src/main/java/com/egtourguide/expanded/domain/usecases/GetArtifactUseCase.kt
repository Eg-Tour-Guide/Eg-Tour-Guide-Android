package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class GetArtifactUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(id: String) = repository.getArtifact(id = id)
}