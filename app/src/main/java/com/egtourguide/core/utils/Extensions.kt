package com.egtourguide.core.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.egtourguide.core.utils.Constants.DATA_STORE_NAME

val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)