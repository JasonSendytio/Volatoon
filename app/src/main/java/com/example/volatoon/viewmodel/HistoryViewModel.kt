package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.HistoryRequest
import com.example.volatoon.model.HistoryResponseData
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    data class HistoryState(
        val loading : Boolean = true,
        val responseData: HistoryResponseData? = null,
        val error : String? = null
    )

    private val _historyState = mutableStateOf(HistoryState())
    val historyState : State<HistoryState> = _historyState

    fun fetchHistory(dataStoreManager: DataStoreManager) {
        _historyState.value = _historyState.value.copy(
            loading = true
        )
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    if (_historyState.value.error != null) {
                        _historyState.value = _historyState.value.copy(
                            error = null
                        )
                    }
                    Log.i("fetch history", "in progress")
                    val response = apiService.getHistory("Bearer $token")
                    _historyState.value = _historyState.value.copy(
                        responseData = response,
                        loading = false
                    )
                    Log.i("fetch history", response.message)
                }
                catch (e: Exception) {
                    _historyState.value = _historyState.value.copy(
                        error = e.message,
                        loading = false
                    )
                    Log.e("fetch history", e.message.toString())
                }
            }
        }
    }

    fun addHistory(dataStoreManager: DataStoreManager, comicId: String) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val response = apiService.postHistory("Bearer $token", komikId = HistoryRequest(comicId))
                    Log.i("add history", response.toString())
                } catch (e: Exception) {
                    Log.e("add history", e.message.toString())
                }
            }
        }
    }

    fun deleteHistory(dataStoreManager: DataStoreManager, historyId: String) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val response = apiService.deleteHistory("Bearer $token", historyId)
                    Log.i("delete history", response.toString())
                }
                catch (e: Exception) {
                    _historyState.value = historyState.value.copy(
                        error = e.message,
                        loading = false
                    )
                    Log.e("delete history", e.toString())
                }
            }
        }
    }
}