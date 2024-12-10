package com.example.urkins.ui.main.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.UserRepository

class AnalyzeViewModelFactory (private val userPreference: UserPreference2, private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyzeViewModel::class.java)) {
            return AnalyzeViewModel(userPreference, repository) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }
}