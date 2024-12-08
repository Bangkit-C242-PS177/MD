package com.example.urkins.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("accessToken")
	val accessToken: String? = null,
	@field:SerializedName("message")
	val message: String? = null,
	@field:SerializedName("user")
	val user: User? = null
)

data class User(
	@field:SerializedName("email")
	val email: String? = null,
	@field:SerializedName("username")
	val username: String? = null
)

