package com.example.urkins.data.response

data class ScanResponse(
	val filename: String? = null,
	val skinConditions: List<List<String?>?>? = null,
	val userId: Int? = null,
	val imageUrl: String? = null,
	val skinType: List<List<String?>?>? = null
)

