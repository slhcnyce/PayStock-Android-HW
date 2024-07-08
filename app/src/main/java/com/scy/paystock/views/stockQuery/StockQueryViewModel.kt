package com.scy.paystock.views.stockQuery

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.navigation.Common
import com.scy.paystock.network.ResponseDataStatus
import com.scy.paystock.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class StockQueryViewModel  : ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message
    private val apiService = RetrofitInstance.api


    private val _showLoading = MutableStateFlow(true)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()

    private val _response = MutableStateFlow<List<Product>?>(null)
    val response: StateFlow<List<Product>?> = _response


    init {
        getProducts()
    }

      private fun getProducts() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                _showLoading.emit(false)
                val response = apiService.getProduct(Common.renterCode.toInt())

                if (response.productDetails.isEmpty()) {

                    _message.emit( "Henüz ürün eklenmemiş")

                    _showDialog.emit(true)
                }

                _response.emit(response.productDetails)


            } catch (e: Exception) {
                _showLoading.value = false
                e.message?.let { Log.d("Hataaaa" , it) }
                _showDialog.tryEmit(true)
                _message.tryEmit("Sistemsel bir hata oluştu lütfen daha sonra tekrar deneyiniz")
                // Handle errors here
            }
        }

    }
    fun dismissDialog() {
        _showDialog.value = false
    }
}

data class Product(
    val productName: String,
    var productQuantity: Int,
    var productPrice: String,
    val productCode: String,
    var productTempQuantity: Int = 1)

data class  ProductDto (
    val renterCode: Long,
    val productDetails: List<Product>

)
