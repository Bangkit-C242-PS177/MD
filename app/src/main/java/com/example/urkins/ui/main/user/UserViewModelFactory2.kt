package com.example.urkins.ui.main.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.repository.HistoryRepository
import com.example.urkins.di.Injection

class UserViewModelFactory2 private constructor(
    private val historyRepository: HistoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel2::class.java)) {
            return UserViewModel2(historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory2? = null

        fun getInstance(context: Context): UserViewModelFactory2 =
            instance ?: synchronized(this) {
                instance ?: UserViewModelFactory2(
                    Injection.provideRepository(context)
                )
            }
    }
}