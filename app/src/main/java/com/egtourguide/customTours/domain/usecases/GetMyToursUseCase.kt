package com.egtourguide.customTours.domain.usecases

import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class GetMyToursUseCase @Inject constructor(
    private val repository: CustomToursRepository
) {
    suspend operator fun invoke() = repository.getMyTours()
}