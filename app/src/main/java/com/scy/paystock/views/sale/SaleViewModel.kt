package com.scy.paystock.views.sale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.navigation.Common
import com.scy.paystock.network.RetrofitInstance
import com.scy.paystock.views.stockQuery.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class SaleViewModel: ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice
    private val apiService = RetrofitInstance.api

    private val _showLoading = MutableStateFlow(true)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()

    private val _response = MutableStateFlow<List<Product>?>(null)
    val response: StateFlow<List<Product>?> get() = _response.asStateFlow()

    private var _productList = MutableStateFlow<List<Product>?>(null)
    val productList: StateFlow<List<Product>?> get() = _productList.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getProduct(Common.renterCode.toInt())
                _showLoading.emit(false)

                if (response.productDetails.isEmpty()) {
                    _message.emit("Henüz ürün eklenmemiş")
                    _showDialog.emit(true)
                }
                for ( index in response.productDetails.indices  ) {
                    response.productDetails[index].productTempQuantity = 1
                }
                _response.emit(response.productDetails)

            } catch (e: Exception) {
                _showLoading.value = false
                e.message?.let { Log.d("Hataaaa", it) }
                _showDialog.tryEmit(true)
                _message.tryEmit("Sistemsel bir hata oluştu, lütfen daha sonra tekrar deneyiniz")
                // Handle errors here
            }
        }
    }
    fun makePayment() {
    _showLoading.value = true
        viewModelScope.launch {
            try {
                val date: String = Date().toString()
                val dto = PaymentDto(productList.value,Common.renterCode, date,totalPrice.value)
                val response = apiService.makePayment(dto
                )
                _showLoading.emit(false)

                if (!response.isSuccess) {
                    _message.emit("Hata")
                    _showDialog.emit(true)
                }
                    _productList.value = null
                _totalPrice.value = 0.0
                getProducts()


            } catch (e: Exception) {
                _showLoading.value = false
                e.message?.let { Log.d("Hataaaa", it) }
                _showDialog.tryEmit(true)
                _message.tryEmit("Sistemsel bir hata oluştu, lütfen daha sonra tekrar deneyiniz")
                // Handle errors here
            }
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun searchProduct(query: String) {
        val currentList = _response.value ?: return
        val filteredList = mutableListOf<Product>()

        _productList.value?.let { productList ->
            productList.forEach { product ->
                filteredList.add(product.copy()) // Ensure to copy products
            }
        }

        var found = false

        for (product in currentList) {
            if (product.productCode.equals(query)) {
                val existingProduct = filteredList.find { it.productCode == product.productCode }

                if (existingProduct != null) {
                    // Product already exists, handle accordingly (e.g., update price)
                } else {
                    found = true
                    _totalPrice.value += product.productPrice.toDouble()
                    filteredList.add(product.copy()) // Add new product to filteredList
                }
            }
        }

        if (found) {
            _productList.value = filteredList
        }
    }

    fun increment(query: String) {
        val currentList = _response.value ?: return
        val filteredList = mutableListOf<Product>()

        _productList.value?.let { productList ->
            productList.forEach { product ->
                filteredList.add(product.copy()) // Ensure to copy products
            }
        }

        var found = false

        for (product in currentList) {
            if (product.productCode.contains(query, ignoreCase = true)) {
                found = true
                val existingProduct = filteredList.find { it.productCode == product.productCode }

                if (existingProduct != null && existingProduct.productTempQuantity < product.productQuantity) {
                    existingProduct.productTempQuantity += 1

                    existingProduct.productQuantity = existingProduct.productTempQuantity
                    _totalPrice.value = (product.productPrice.toInt() * existingProduct.productTempQuantity).toDouble()
                }
            }
        }

        if (found) {
            _productList.value = filteredList
        }
    }

    fun decrement(query: String) {
        val currentList = _response.value ?: return
        val filteredList = mutableListOf<Product>()

        _productList.value?.let { productList ->
            productList.forEach { product ->
                filteredList.add(product.copy()) // Ensure to copy products
            }
        }

        var found = false

        for (product in currentList) {
            if (product.productCode.contains(query, ignoreCase = true)) {
                found = true
                val existingProduct = filteredList.find { it.productCode == product.productCode }

                if (existingProduct != null && existingProduct.productTempQuantity >= 1) {
                    existingProduct.productTempQuantity -= 1

                    existingProduct.productQuantity = existingProduct.productTempQuantity
                    _totalPrice.value = (product.productPrice.toInt() * existingProduct.productTempQuantity).toDouble()
                    if (existingProduct.productTempQuantity == 0) {
                        filteredList.remove(existingProduct)
                    }
                }
            }
        }

        if (found) {
            _productList.value = filteredList
        }
    }

}
data class PaymentDto(
    val productDetails: List<Product>?,
    val renterCode: Long,
    val date: String,
    val price: Double
)