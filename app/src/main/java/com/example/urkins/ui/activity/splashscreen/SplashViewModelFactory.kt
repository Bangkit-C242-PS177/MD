package com.example.urkins.ui.activity.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.UserRepository

class SplashViewModelFactory(private val userPreference: UserPreference2, private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userPreference, repository) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }

}