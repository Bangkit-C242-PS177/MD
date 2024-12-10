package com.example.urkins.ui.activity.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.pref.UserPreference

class SplashViewModelFactory(private val userPreference: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userPreference) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }

}