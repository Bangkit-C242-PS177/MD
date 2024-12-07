package com.example.urkins.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArticlesResponse (
    val articleTitle: String,
    val articleDate: String,
    val articleAuthor: String,
    val articlePhoto: String?
) : Parcelable