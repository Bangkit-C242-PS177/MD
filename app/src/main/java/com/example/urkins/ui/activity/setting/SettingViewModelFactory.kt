package com.example.urkins.ui.activity.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.di.Injection
import com.example.urkins.ui.activity.splashscreen.SplashViewModel

class SettingViewModelFactory(private val repository: UserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repository) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): SettingViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SettingViewModelFactory::class.java) {
                    INSTANCE = SettingViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as SettingViewModelFactory
        }
    }
}


