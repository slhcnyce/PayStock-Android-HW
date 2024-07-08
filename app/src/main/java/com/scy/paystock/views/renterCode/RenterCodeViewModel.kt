package com.scy.paystock.views.renterCode


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.network.ResponseDataStatus
import com.scy.paystock.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
class RenterCodeViewModel : ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message
    private val apiService = RetrofitInstance.api



    private val _response = MutableStateFlow<ResponseDataStatus?>(null)
    val response: StateFlow<ResponseDataStatus?> = _response
    fun checkRenterCode(code: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.checkRenterCode(renter = RenterDto(id = code))
                _response.value = response
                if (!response.isSuccess) {
                    _message.value = "Sistemde kayıtlı kiracı kodu giriniz"
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


data class RenterDto(
    val id: Long
)



