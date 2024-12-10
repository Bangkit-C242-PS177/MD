package com.example.urkins.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.urkins.data.pref.UserModel
import com.example.urkins.data.repository.UserRepository

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}