package com.egtourguide.core.domain.repository

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {

    suspend fun <T> saveInDataStore(key: Preferences.Key<T>, value: T)

    suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T?

    suspend fun <T> deleteFromDataStore(key: Preferences.Key<T>)
}