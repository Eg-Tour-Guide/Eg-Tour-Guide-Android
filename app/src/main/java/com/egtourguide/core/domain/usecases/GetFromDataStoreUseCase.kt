package com.egtourguide.core.domain.usecases

import androidx.datastore.preferences.core.Preferences
import com.egtourguide.core.domain.repository.DataStoreRepository
import javax.inject.Inject

class GetFromDataStoreUseCase @Inject constructor(private val repository: DataStoreRepository) {

    suspend operator fun <T> invoke(key: Preferences.Key<T>) =
        repository.getFromDataStore(key = key)
}