package com.example.volatoon.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StoreUserId(private val context: Context) {

    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userId")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    //get the saved email
    val getIdUser: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: "ID_User"
        }

    //save email into datastore
    suspend fun saveIdUser(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = name
        }
    }
}