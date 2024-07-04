package com.egtourguide.expanded.domain.usecases

import com.egtourguide.expanded.data.dto.body.ReviewRequestBody
import com.egtourguide.expanded.domain.repository.ExpandedRepository
import javax.inject.Inject

class ReviewPlaceUseCase @Inject constructor(
    private val repository: ExpandedRepository
) {
    suspend operator fun invoke(id: String, rating: Int, review: String) =
        repository.sendPlaceReview(
            placeId = id,
            requestBody = ReviewRequestBody(
                rating = rating,
                review = review
            )
        )
}