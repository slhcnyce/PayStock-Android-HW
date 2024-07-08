package com.scy.paystock.views.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.network.ResponseDataStatus
import com.scy.paystock.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val renterCode: Long) : ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message


    private val apiService = RetrofitInstance.api

    private val _username = mutableStateOf("")
    val username: String by _username

    private val _password = mutableStateOf("")
    val password: String by _password





    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    private val _response = MutableStateFlow<ResponseDataStatus?>(null)
    val response: StateFlow<ResponseDataStatus?> = _response
    fun login(username: String, password: String) {
        // Simulate login logic (replace with actual authentication)
         Log.d("credientials" , username + password + renterCode)
        viewModelScope.launch {


            try {
                val response = apiService.authentication(LoginDto(username,
                    password,
                    renterCode = renterCode))
                _response.value = response
                if (!response.isSuccess) {
                    _message.value = "Kullanıcı adı yada şifre bulunamadı"
                    _showDialog.value = true
                }

            } catch (e: Exception) {
                e.message?.let { Log.d("Hataaaa" , it) }
                _showDialog.value = true
                _message.value = "Sistemsel bir hata oluştu lütfen daha sonra tekrar deneyiniz"
                // Handle errors here
            }
        }
    }
    fun dismissDialog() {
        _showDialog.value = false
    }
}

data class LoginResponse(val isSuccess: Boolean) {

}
data class LoginDto(
    val username: String,
    val password: String,
    val renterCode: Long
)
