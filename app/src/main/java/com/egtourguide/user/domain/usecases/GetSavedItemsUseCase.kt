package com.egtourguide.user.domain.usecases

import com.egtourguide.user.domain.repository.UserRepository
import javax.inject.Inject

class GetSavedItemsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getSavedList()
}