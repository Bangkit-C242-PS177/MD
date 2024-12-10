package com.example.urkins.ui.activity.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urkins.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel (private val repository: UserRepository) : ViewModel(){
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}