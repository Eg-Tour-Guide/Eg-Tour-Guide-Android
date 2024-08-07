package com.egtourguide.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.egtourguide.core.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun <T> saveInDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    override suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T? {
        return dataStore.data
            .catch { throwable -> throwable.printStackTrace() }
            .map { it[key] }
            .firstOrNull()
    }

    override suspend fun <T> deleteFromDataStore(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key)
        }
    }
}