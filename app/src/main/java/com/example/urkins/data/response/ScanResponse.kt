package com.example.urkins.data.response

data class ScanResponse(
	val filename: String,
	val skinConditions: List<List<String>>,
	val userId: Int,
	val imageUrl: String,
	val skinType: List<List<String>>
)

