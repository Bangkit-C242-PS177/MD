package com.example.urkins.data.response

data class LoginResponse(
	val accessToken: String? = null,
	val message: String? = null,
	val user: User? = null
)

data class User(
	val email: String? = null,
	val username: String? = null
)

