package com.example.urkins.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val userToken = stringPreferencesKey("user_token")
    private val userEmail = stringPreferencesKey("user_email")
    private val userName = stringPreferencesKey("user_name")

    suspend fun getUserToken(): String {
        val preferences = dataStore.data.first()
        val token = preferences[userToken] ?: ""
        Log.d("UserPreference", "Token diambil: $token")
        return token
    }

    suspend fun getUserId(): String {
        val preferences = dataStore.data.first()
        return preferences[userEmail] ?: ""
    }

    suspend fun getUserName(): String {
        val preferences = dataStore.data.first()
        return preferences[userName] ?: ""
    }

    suspend fun saveUserData(token: String, email: String, name: String) {
        dataStore.edit { preferences ->
            preferences[userToken] = token
            preferences[userEmail] = email
            preferences[userName] = name
        }
        Log.d("UserPreference", "Data disimpan: Token=$token, Email=$email, Name=$name")
    }

    suspend fun clearUserData() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}