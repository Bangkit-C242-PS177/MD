package com.example.urkins.ui.activity.result

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urkins.data.local.entity.HistoryEntity
import com.example.urkins.data.repository.HistoryRepository

class ResultViewModel (private val historyRepository: HistoryRepository) : ViewModel() {

    private val _skinConditions = MutableLiveData<List<List<String>>>()
    val skinConditions: LiveData<List<List<String>>> = _skinConditions

    private val _skinType = MutableLiveData<List<List<String>>>()
    val skinType: LiveData<List<List<String>>> = _skinType

    fun setData(conditions: List<List<String>>, types: List<List<String>>) {
        _skinConditions.postValue(conditions)
        _skinType.postValue(types)
    }

    private val _snackBar = MutableLiveData<String>()
    val snackBar: LiveData<String> = _snackBar

    fun saveHistory(imageUri: Uri, prediction: String, prediction2: String) {
        val imageToString = imageUri.toString()

        historyRepository.getHistoryByImageUri(imageToString) { result ->
            if (result == null) {
                val historyEntity = HistoryEntity(
                    imageUri = imageUri,
                    prediction = prediction,
                    prediction2 = prediction2,

                    )
                historyRepository.saveHistory(historyEntity)
                _snackBar.value = "History Saved"
            } else {
                _snackBar.value = "Sudah pernah dianalisa"
            }
        }
    }
}