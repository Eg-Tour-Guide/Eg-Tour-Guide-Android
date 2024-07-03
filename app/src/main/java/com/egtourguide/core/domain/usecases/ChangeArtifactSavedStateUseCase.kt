package com.egtourguide.core.domain.usecases

import com.egtourguide.core.domain.repository.CommonRepository
import javax.inject.Inject

class ChangeArtifactSavedStateUseCase @Inject constructor(
    private val repository: CommonRepository
) {
    suspend operator fun invoke(artifactId: String) =
        repository.changeArtifactSavedState(artifactId = artifactId)
}