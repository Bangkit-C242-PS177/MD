package com.example.urkins.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SkincareRecommendation(
    val skincareName: String,
    val skincareCategory: String,
    val skincareDescription: String,
    val skincareHowToUse: String,
    val skincareIngredients: String,
    val skincarePhoto: String?
) : Parcelable