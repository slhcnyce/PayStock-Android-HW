package com.scy.paystock.views.saleReport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.navigation.Common
import com.scy.paystock.network.RetrofitInstance
import com.scy.paystock.views.stockQuery.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SaleReportViewModel: ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message

    private val _showLoading = MutableStateFlow(true)
    val showLoading  = _showLoading.asSharedFlow()

    private val apiService = RetrofitInstance.api

    private val _response = MutableStateFlow<List<PaymentDetailDto>?>(null)
    val response: StateFlow<List<PaymentDetailDto>?> get() = _response.asStateFlow()
    init {
        getSaleReports()
    }
      private fun getSaleReports() {

          _showLoading.value = true
          viewModelScope.launch {
              try {
                  val response = apiService.getSaleReports(Common.renterCode)
                  _showLoading.emit(false)
                  if (response.paymentDetails.isEmpty()) {
                      _message.emit("Henüz satış yapılmamış")
                      _showDialog.emit(true)
                  }

                  _response.emit(response.paymentDetails)

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
}
data class SaleReportDto (
    val paymentDetails: List<PaymentDetailDto>
)
data class  PaymentDetailDto(
    val price: Double,
    val date : String
)