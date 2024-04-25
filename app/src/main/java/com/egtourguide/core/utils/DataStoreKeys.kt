package com.egtourguide.core.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val TOKEN_KEY = stringPreferencesKey("USER_TOKEN")
    val IS_LOGGED_KEY = booleanPreferencesKey("IS_LOGGED")
}