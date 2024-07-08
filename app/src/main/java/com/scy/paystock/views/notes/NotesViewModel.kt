package com.scy.paystock.views.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scy.paystock.navigation.Common
import com.scy.paystock.network.RetrofitInstance
import com.scy.paystock.views.stockQuery.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private val _message = MutableStateFlow("false")
    val message: StateFlow<String> = _message
    private val apiService = RetrofitInstance.api


    private val _showLoading = MutableStateFlow(true)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()

    private val _response = MutableStateFlow<List<NoteDetailDto>?>(null)
    val response: StateFlow<List<NoteDetailDto>?> = _response


    init {
        getNotes()
    }

    private fun getNotes() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                _showLoading.emit(false)
                val response = apiService.getNotes(Common.renterCode.toInt())

                if (response.isEmpty()) {

                    return@launch
                }

                _response.emit(response)


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
    fun deleteNoteDetail(noteId: Long) {
        viewModelScope.launch {
            _showLoading.value = true
            viewModelScope.launch {


                try {
                    _showLoading.emit(false)
                    val response = apiService.deleteNote(renterCode = Common.renterCode  , id = noteId )

                    if (response.isSuccess) {
                        _response.value = _response.value?.filterNot { it.id == noteId }
                    } else {
                        _message.emit("Error")
                        _showDialog.emit(true)
                    }


                } catch (e: Exception) {
                    _showLoading.value = false
                    e.message?.let { Log.d("Hataaaa" , it) }
                    _showDialog.tryEmit(true)
                    _message.tryEmit("Sistemsel bir hata oluştu lütfen daha sonra tekrar deneyiniz")
                    // Handle errors here
                }

            }


        }
    }

    fun createNoteDetail(value: String) {
        viewModelScope.launch {
            _showLoading.value = true
            viewModelScope.launch {


                try {
                    _showLoading.emit(false)
                    val response = apiService.createNote(CreateNoteDto(Common.renterCode,
                        value) )

                    if (response.isSuccess) {
                       getNotes()
                    } else {
                        _message.emit("Error")
                        _showDialog.emit(true)
                    }


                } catch (e: Exception) {
                    _showLoading.value = false
                    e.message?.let { Log.d("Hataaaa" , it) }
                    _showDialog.tryEmit(true)
                    _message.tryEmit("Sistemsel bir hata oluştu lütfen daha sonra tekrar deneyiniz")
                    // Handle errors here
                }

            }


        }
    }
}




data class NoteDetailDto (
    val id: Long,
    val note: String
)
data class CreateNoteDto (
    val renterCode: Long,
    val note: String
)
