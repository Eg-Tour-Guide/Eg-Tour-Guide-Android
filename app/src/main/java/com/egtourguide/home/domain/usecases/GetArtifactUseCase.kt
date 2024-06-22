package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetArtifactUseCase @Inject constructor(private val repository: HomeRepository) {

    suspend operator fun invoke(id: String) = repository.getArtifact(id = id)
}