package com.example.urkins.ui.activity.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.urkins.R
import com.example.urkins.data.repository.RegisterRepository
import com.example.urkins.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(
    application: Application,
    private val registerRepository: RegisterRepository
) : AndroidViewModel(application) {
    private val _showSuccessDialog = MutableLiveData<String>()
    val showSuccessDialog: LiveData<String>
        get() = _showSuccessDialog

    private val _showErrorDialog = MutableLiveData<String>()
    val showErrorDialog: LiveData<String>
        get() = _showErrorDialog

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun registerUser(username: String, email: String, password: String, confirm_password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerRepository.registerUser(username, email, password, confirm_password)
                _loading.postValue(false)
                _showSuccessDialog.postValue(response.message ?: getApplication<Application>().getString(R.string.register_succes))
            } catch (e: HttpException) {
                _loading.postValue(false)
                handleError(e)
            } catch (e: Exception) {
                _loading.postValue(false)
                _showErrorDialog.postValue(getApplication<Application>().getString(R.string.register_failed_dialog))
            }
        }
    }

    private fun handleError(e: HttpException) {
        try {
            val jsonInString = e.response()?.errorBody()?.string()
            if (jsonInString != null && jsonInString.startsWith("{")) {
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage = errorBody.message ?: getApplication<Application>().getString(R.string.register_error)
                _showErrorDialog.postValue(errorMessage)
            } else {
                _showErrorDialog.postValue("Terjadi kesalahan: $jsonInString")
            }
        } catch (e: Exception) {
            _showErrorDialog.postValue(getApplication<Application>().getString(R.string.register_failed_dialog))
        }
    }
}