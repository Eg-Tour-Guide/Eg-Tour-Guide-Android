package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(eventId: String) = repository.getEvent(eventId = eventId)
}