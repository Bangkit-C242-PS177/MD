package com.example.urkins.data.repository

import com.example.urkins.data.response.RegisterResponse
import com.example.urkins.data.retrofit.ApiService

class RegisterRepository (
    private val apiService: ApiService
) {
    suspend fun registerUser(registerRequest: ApiService.RegisterRequest): RegisterResponse {
        return apiService.registerUser(registerRequest)
    }

    companion object {

        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(
            apiService: ApiService
        ): RegisterRepository =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository(apiService)
            }.also { instance = it }
    }
}