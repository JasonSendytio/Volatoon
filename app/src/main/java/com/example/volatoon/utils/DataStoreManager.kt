package com.example.volatoon.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.android.jwt.JWT
import com.example.volatoon.model.User
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(val context: Context){

    companion object{
        val USER_ID = stringPreferencesKey("USER_ID")
    }

    suspend fun saveToDataStore(token : String){
        val jwt = JWT(token)

        context.preferenceDataStore.edit {
            it[USER_ID] = jwt.getClaim("userId").toString()
        }
    }

    fun getFromDataStore() = context.preferenceDataStore.data.map {
        User(userId = it[USER_ID]?:"")
    }

    suspend fun clearDataStore() = context.preferenceDataStore.edit {
        it.clear()
    }

}