package com.example.volatoon.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.android.jwt.JWT
import com.example.volatoon.model.Auth
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(private val context: Context){

    companion object{
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
    }

    suspend fun saveToDataStore(token : String){
        val jwt = JWT(token)

        val authToken : String =jwt.toString()

        context.preferenceDataStore.edit {
            it[AUTH_TOKEN] = authToken
        }
    }

    fun getFromDataStore() = context.preferenceDataStore.data.map {
        Auth(authToken = it[AUTH_TOKEN]?:"")
    }

    suspend fun clearDataStore() = context.preferenceDataStore.edit {
        it.clear()
    }

}