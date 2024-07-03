package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.data.dto.body.ReviewRequestBody
import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class ReviewTourUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(id: String, requestBody: ReviewRequestBody) =
        repository.sendTourReview(id, requestBody)
}