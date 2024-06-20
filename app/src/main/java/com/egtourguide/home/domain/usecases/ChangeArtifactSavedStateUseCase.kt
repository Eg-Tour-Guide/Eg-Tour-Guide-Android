package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class ChangeArtifactSavedStateUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(artifactId: String) = homeRepository.changeArtifactSavedState(
        artifactId = artifactId
    )
}