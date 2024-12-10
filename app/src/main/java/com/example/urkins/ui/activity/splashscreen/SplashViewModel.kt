package com.example.urkins.ui.activity.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.urkins.data.pref.UserModel
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.UserRepository
import kotlinx.coroutines.launch

class SplashViewModel(private val userPreference: UserPreference2, private val repository: UserRepository) : ViewModel() {
    private val _isUserTokenAvailable = MutableLiveData<Boolean>()
    val isUserTokenAvailable: LiveData<Boolean>
        get() = _isUserTokenAvailable

    init {
        checkUserToken()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private fun checkUserToken() {
        viewModelScope.launch {
            val token = userPreference.getUserToken()
            _isUserTokenAvailable.value = token.isNotEmpty()
        }
    }
}