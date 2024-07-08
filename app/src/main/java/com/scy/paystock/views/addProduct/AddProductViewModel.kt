package com.scy.paystock.views.addProduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.navigation.Common
import com.scy.paystock.network.ResponseDataStatus
import com.scy.paystock.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class  AddProductViewModel : ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message
    private val _title = MutableStateFlow("false")
    val title: StateFlow<String> = _title
    private val apiService = RetrofitInstance.api
    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _response = MutableStateFlow<ResponseDataStatus?>(null)
    val response: StateFlow<ResponseDataStatus?> = _response


    fun createProduct(dto:AddProductDto) {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                _showLoading.value = false
                val response = apiService.addProduct(dto)
                _response.value = response
                if (!response.isSuccess) {
                    _title.value = "Hata"
                    _message.value = "Sistemsel bir hata oluştu lütfen daha sonra tekrar deneyiniz"
                    _showDialog.value = true
                }
                else {
                    _title.value = "Başarılı"
                    _message.value = "Ürün başarı ile eklendi."
                    _showDialog.value = true
                }

            } catch (e: Exception) {
                _showLoading.value = false
                _title.value = "Hata"
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

data class AddProductDto(
    val productName: String,
    val productCode: String,
    val productQuantity: String,
    val productPrice: String,
    val renterCode: Long = Common.renterCode
)
