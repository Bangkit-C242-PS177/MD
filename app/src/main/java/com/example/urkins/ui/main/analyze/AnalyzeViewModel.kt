package com.example.urkins.ui.main.analyze

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyzeViewModel : ViewModel() {

    private val _selectUriImage = MutableLiveData<Uri?>()
    val selectUriImage: LiveData<Uri?> = _selectUriImage

    fun setSelectImageUri(uri: Uri?) {
        _selectUriImage.value = uri
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}