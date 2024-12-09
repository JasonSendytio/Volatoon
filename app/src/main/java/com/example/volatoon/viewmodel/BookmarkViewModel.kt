package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.BookmarkRequest
import com.example.volatoon.model.apiService
import com.example.volatoon.model.BookmarkResponseData
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.Exception

class BookmarkViewModel : ViewModel(){
    private val _bookmarkstate = mutableStateOf(BookmarkState())
    val bookmarkstate : State<BookmarkState> = _bookmarkstate

    private val _addBookmarkstate = mutableStateOf(AddBookmarkState())
    val addBookmarkstate : State<AddBookmarkState> = _addBookmarkstate

    fun fetchUserBookmark(dataStoreManager : DataStoreManager){
        _bookmarkstate.value = _bookmarkstate.value.copy(
            loading = true
        )

        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.fetchBookmark(bearerToken)

                    _bookmarkstate.value = _bookmarkstate.value.copy(
                        loading = false,
                        responseData = response,
                        error = null
                    )
                    Log.i("fetch bookmark", response.message)

                } catch (e : Exception){
                    _bookmarkstate.value = _bookmarkstate.value.copy(
                        loading = false,
                        error = "error fetching profile ${e.message}"
                    )
                    Log.e("fetch bookmark", e.message.toString())
                }
            }
        }
    }

    fun addUserBookmark(dataStoreManager : DataStoreManager, comicId : String){
        _addBookmarkstate.value = _addBookmarkstate.value.copy(
            loading = true
        )

        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.postBookmark(token = bearerToken, komikId = BookmarkRequest(komikId = comicId))

                    println("asd")

                    _addBookmarkstate.value = _addBookmarkstate.value.copy(
                        loading = false,
                        error = null
                    )
                    Log.i("add bookmark", response.toString())

                } catch (e : Exception){
                    _addBookmarkstate.value = _addBookmarkstate.value.copy(
                        loading = false,
                        error = "error fetching profile ${e.message}"
                    )
                    Log.e("add bookmark", e.message.toString())
                }
            }
        }
    }

    fun deleteUserBookmark(dataStoreManager : DataStoreManager, bookmarkId : String){
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.deleteBookmark(bearerToken, bookmarkId)

                    fetchUserBookmark(dataStoreManager)

                } catch (e : Exception){
                    _bookmarkstate.value = _bookmarkstate.value.copy(
                        loading = false,
                        error = "error fetching bookmark ${e.message}"
                    )
                }
            }
        }
    }

    data class AddBookmarkState(
        val loading : Boolean = false,
        val error : String? = null
    )

    data class BookmarkState(
        val loading : Boolean = true,
        val responseData: BookmarkResponseData? = null,
        val error : String? = null
    )
}