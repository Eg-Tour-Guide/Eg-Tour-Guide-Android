package com.egtourguide.core.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val TOKEN_KEY = stringPreferencesKey("USER_TOKEN")
    val IS_LOGGED_KEY = booleanPreferencesKey("IS_LOGGED")
    val NOTIFICATIONS_KEY = booleanPreferencesKey("NOTIFICATIONS")
    val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    val USER_EMAIL_KEY = stringPreferencesKey("USER_EMAIL")
    val USER_PHONE_KEY = stringPreferencesKey("USER_PHONE")
}