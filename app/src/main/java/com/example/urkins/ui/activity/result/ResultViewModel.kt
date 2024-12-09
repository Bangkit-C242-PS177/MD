package com.example.urkins.ui.activity.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    private val _skinConditions = MutableLiveData<List<List<String>>>()
    val skinConditions: LiveData<List<List<String>>> = _skinConditions

    private val _skinType = MutableLiveData<List<List<String>>>()
    val skinType: LiveData<List<List<String>>> = _skinType

    fun setData(conditions: List<List<String>>, types: List<List<String>>) {
        _skinConditions.postValue(conditions)
        _skinType.postValue(types)
    }
}