package com.example.urkins.ui.main.analyze

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.urkins.data.pref.UserModel
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.data.response.ScanResponse
import com.example.urkins.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AnalyzeViewModel (private val userPreference: UserPreference2, private val repository: UserRepository) : ViewModel() {

    private val _selectUriImage = MutableLiveData<Uri?>()
    val selectUriImage: LiveData<Uri?> = _selectUriImage

    private val _skinResponse = MutableLiveData<ScanResponse>()
    val skinResponse: LiveData<ScanResponse> = _skinResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

//    private val apiService by lazy { ApiConfig.getApiService(userToken) }

    fun setSelectImageUri(uri: Uri?) {
        _selectUriImage.value = uri
    }

    private val _isUserTokenAvailable = MutableLiveData<Boolean>()
    val isUserTokenAvailable: LiveData<Boolean> = _isUserTokenAvailable

//    init {
//        checkUserToken()
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadImage(uri: Uri, userId: Int) {
        val file = File(uri.path!!)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())

        ApiConfig.api().analyzeSkin(body, userIdBody).enqueue(object : Callback<ScanResponse> {
            override fun onResponse(call: Call<ScanResponse>, response: Response<ScanResponse>) {
                if (response.isSuccessful) {
                    _skinResponse.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                _errorMessage.postValue("Failed: ${t.message}")
            }
        })
    }

//    private fun checkUserToken() {
//        viewModelScope.launch {
//            val token = userPreference.getUserToken()
//            _isUserTokenAvailable.value = token.isNotEmpty()
//        }
//    }
fun checkUserToken(tokenAvailable: Boolean) {
    _isUserTokenAvailable.postValue(tokenAvailable)
}
}