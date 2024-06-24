package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(eventId: String) = repository.getEvent(eventId = eventId)
}