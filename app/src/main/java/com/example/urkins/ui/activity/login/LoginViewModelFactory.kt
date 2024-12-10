package com.example.urkins.ui.activity.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.LoginRepository
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.di.Injection

class LoginViewModelFactory private constructor(
    private val application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference2,
    private val repository: UserRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application, loginRepository, userPreference,repository) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(
            application: Application,
            userPreference: UserPreference2,
            repository: UserRepository
        ): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    application,
                    Injection.loginRepository(application),
                    userPreference,
                    repository
                )
            }.also { instance = it }
    }
}