package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetLandmarkUseCase @Inject constructor(private val repository: HomeRepository) {

    suspend operator fun invoke(id: String) = repository.getLandmark(id = id)
}