package com.example.urkins.ui.activity.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.urkins.R
import com.example.urkins.data.pref.UserPreference
import com.example.urkins.data.repository.LoginRepository
import com.example.urkins.data.response.LoginResponse
import com.example.urkins.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel (
    application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
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

    fun loginUser(email: String, password: String) {
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginRequest = ApiService.LoginRequest(email, password)
                val response = loginRepository.login(loginRequest)
                val userToken = response.accessToken
                val loginResult = response.user
                val userEmail = loginResult?.email
                val username = loginResult?.username
                _loading.postValue(false)
                _showSuccessDialog.postValue(getApplication<Application>().getString(R.string.login_succes_dialog))
                userToken?.let { token ->
                    userEmail?.let { email ->
                        username?.let { name ->
                            saveUserData(name, email, token)
                        }
                    }
                }
//                saveUserData(response)
            } catch (e: HttpException) {
                _loading.postValue(false)
                handleError(e)
            } catch (e: Exception) {
                _loading.postValue(false)
                _showErrorDialog.postValue(getApplication<Application>().getString(R.string.login_failed_dialog))
            }
        }
    }

    private fun handleError(e: HttpException) {
        try {
            val jsonInString = e.response()?.errorBody()?.string()
            if (jsonInString != null && jsonInString.startsWith("{")) {
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message ?: getApplication<Application>().getString(R.string.login_error_messege)
                _showErrorDialog.postValue(errorMessage)
            } else {
                _showErrorDialog.postValue("Terjadi kesalahan: $jsonInString")
            }
        } catch (e: Exception) {
            _showErrorDialog.postValue(getApplication<Application>().getString(R.string.login_failed_dialog))
        }
    }

    private fun saveUserData(userEmail: String, username: String, userToken: String) {
        viewModelScope.launch {
            userPreference.saveUserData(token = userToken, email = userEmail , name = username)
        }
    }

}