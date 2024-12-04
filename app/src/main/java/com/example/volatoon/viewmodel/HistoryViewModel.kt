package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.DetailComic
import com.example.volatoon.model.HistoryResponseData
import com.example.volatoon.model.apiService
import com.example.volatoon.model.comicApiService
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    data class HistoryState(
        val loading : Boolean = true,
        val responseData: HistoryResponseData? = null,
        val error : String? = null
    )

    data class DetailComicState(
        val loading : Boolean = true,
        val detailComic : DetailComic? = null,
        val error : String? = null
    )

    private val _historyState = mutableStateOf(HistoryState())
    val historyState : State<HistoryState> = _historyState
    private val _comicDetail = mutableStateOf(DetailComicState())
    val comicDetail : State<DetailComicState> = _comicDetail

    fun fetchUserHistory(dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val responseHistory = apiService.getHistory("Bearer $token")
                    _historyState.value = historyState.value.copy(
                        responseData = responseHistory,
                        loading = false
                    )
                }
                catch (e: Exception) {
                    _historyState.value = historyState.value.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }
        }
    }

    fun deleteHistory(dataStoreManager: DataStoreManager, historyId: String) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val response = apiService.deleteHistory("Bearer $token", historyId = historyId)
                    _historyState.value = historyState.value.copy(
                        responseData = response,
                        loading = false
                    )
                }
                catch (e: Exception) {
                    _historyState.value = historyState.value.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }
        }
    }

    fun fetchComicDetail(comicId: String) {
        viewModelScope.launch {
            val response = comicApiService.getDetailComic(comicId)
            _comicDetail.value = comicDetail.value.copy(
                detailComic = response,
                loading = false
            )
        }
    }
}