package com.example.urkins.di

import android.content.Context
import com.example.urkins.data.pref.UserPreference
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.LoginRepository
import com.example.urkins.data.repository.RegisterRepository
import com.example.urkins.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

object Injection {
    fun registerRepository(context: Context): RegisterRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return RegisterRepository.getInstance(apiService)
    }

    fun loginRepository(context: Context): LoginRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return LoginRepository.getInstance(apiService)
    }
}