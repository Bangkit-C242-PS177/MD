package com.example.urkins.data.retrofit

import com.example.urkins.data.response.LoginResponse
import com.example.urkins.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
//    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
//        @Field("username") username: String,
//        @Field("email") email: String,
//        @Field("password") password: String,
//        @Field("confirm_password") confirm_password: String
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
}