package com.example.urkins.di

import android.content.Context
import com.example.urkins.data.local.room.HistoryDatabase
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.HistoryRepository
import com.example.urkins.data.repository.LoginRepository
import com.example.urkins.data.repository.RegisterRepository
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.data.retrofit.ApiConfig
import com.example.urkins.util.AppExecutors
import kotlinx.coroutines.runBlocking

object Injection {
    fun registerRepository(context: Context): RegisterRepository {
        val pref = UserPreference2.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return RegisterRepository.getInstance(apiService)
    }

    fun loginRepository(context: Context): LoginRepository {
        val pref = UserPreference2.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return LoginRepository.getInstance(apiService)
    }

    fun provideRepository(context: Context): HistoryRepository {
        val database = HistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        val appExecutors = AppExecutors()
        return HistoryRepository.getInstance(dao, appExecutors)
    }

}