package com.example.urkins.data.repository

import com.example.urkins.data.response.RegisterResponse
import com.example.urkins.data.retrofit.ApiService

class RegisterRepository (
    private val apiService: ApiService
) {
    suspend fun registerUser(username: String, email: String, password: String, confirm_password: String): RegisterResponse {
        return apiService.registerUser(username, email, password, confirm_password)
    }
//
//    suspend fun registerUser(username: String, email: String, password: String, confirm_password: String): String {
//        val response = apiService.registerUser(username, email, password, confirm_password)
//        return response.string() // Baca respons langsung sebagai string
//    }

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