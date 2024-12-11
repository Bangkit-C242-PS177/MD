package com.example.urkins.data.retrofit

import com.example.urkins.data.response.LoginResponse
import com.example.urkins.data.response.RegisterResponse
import com.example.urkins.data.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    data class LoginRequest(
        val email: String,
        val password: String
    )

    data class RegisterRequest(
        val username: String,
        val email: String,
        val password: String,
        val confirm_password: String
    )


    @Multipart
    @POST("scan")
    fun analyzeSkin(
        @Part file: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Call<ScanResponse>
}