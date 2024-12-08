package com.example.urkins.ui.main.analyze

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urkins.data.pref.UserPreference
import kotlinx.coroutines.launch

class AnalyzeViewModel (private val userPreference: UserPreference) : ViewModel() {

    private val _selectUriImage = MutableLiveData<Uri?>()
    val selectUriImage: LiveData<Uri?> = _selectUriImage

    fun setSelectImageUri(uri: Uri?) {
        _selectUriImage.value = uri
    }

    private val _isUserTokenAvailable = MutableLiveData<Boolean>()
    val isUserTokenAvailable: LiveData<Boolean> = _isUserTokenAvailable

    init {
        checkUserToken()
    }

    private fun checkUserToken() {
        viewModelScope.launch {
            val token = userPreference.getUserToken()
            _isUserTokenAvailable.value = token.isNotEmpty()
        }
    }
}