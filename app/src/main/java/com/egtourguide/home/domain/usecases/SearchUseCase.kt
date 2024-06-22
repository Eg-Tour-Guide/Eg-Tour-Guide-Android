package com.egtourguide.home.domain.usecases

import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(query: String) = homeRepository.search(query = query)
}