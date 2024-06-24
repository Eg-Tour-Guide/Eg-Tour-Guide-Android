package com.egtourguide.home.domain.usecases

import com.egtourguide.home.data.body.ReviewRequestBody
import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class ReviewTourUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(id: String, requestBody: ReviewRequestBody) =
        homeRepository.sendTourReview(id, requestBody)
}