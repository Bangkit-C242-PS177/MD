package com.example.urkins.data.retrofit

import com.example.urkins.data.response.LoginResponse
import com.example.urkins.data.response.RegisterResponse
import com.example.urkins.data.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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


    @Multipart
    @POST("scan") // Ganti dengan endpoint sesuai API Anda
    fun analyzeSkin(
        @Part file: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Call<ScanResponse>
}