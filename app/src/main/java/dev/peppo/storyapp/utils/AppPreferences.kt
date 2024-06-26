package dev.peppo.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app")

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val token = stringPreferencesKey("token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token].toString()
        }
    }

    suspend fun saveLoginSession(userToken: String) {
        dataStore.edit { pref ->
            pref[token] = userToken
        }
    }

    suspend fun deleteLoginSession() {
        dataStore.edit { pref ->
            pref.remove(token)
        }
    }

    companion object {
        @Volatile
        private var INSTACE: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTACE ?: synchronized(this) {
                val instance = AppPreferences(dataStore)
                INSTACE = instance
                instance
            }
        }
    }
}