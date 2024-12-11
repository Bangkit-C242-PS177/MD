package com.example.urkins.ui.main.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.repository.HistoryRepository
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.di.Injection

class UserViewModelFactory(
    private val repository: UserRepository,
//    private val historyRepository: HistoryRepository
) : ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class"+ modelClass.name)
    }

//    companion object {
//        @Volatile
//        private var instance: UserViewModelFactory? = null
//
//        fun getInstance(context: Context): UserViewModelFactory =
//            instance ?: synchronized(this) {
//                instance ?: UserViewModelFactory(
//                    Injection.provideRepository(context),
//                    Injection.provideRepository2(context)
//                ) .also { instance = it }
//            }
//    }
}
