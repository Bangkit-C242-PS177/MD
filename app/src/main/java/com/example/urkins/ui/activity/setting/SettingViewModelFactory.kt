package com.example.urkins.ui.activity.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.di.Injection
import com.example.urkins.ui.activity.splashscreen.SplashViewModel

class SettingViewModelFactory(private val userPreference: UserPreference2, private val repository: UserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(repository) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingViewModelFactory? = null

        @JvmStatic
        fun getInstance(
//            context: Context,
            userPreference: UserPreference2,
            repository: UserRepository
        ): SettingViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SettingViewModelFactory::class.java) {
                    INSTANCE = SettingViewModelFactory(
                        userPreference,repository, )
                }
            }
            return INSTANCE as SettingViewModelFactory
        }
    }
}


