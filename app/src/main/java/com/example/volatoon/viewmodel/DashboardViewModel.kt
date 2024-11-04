package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.ComicApiService
import com.example.volatoon.model.comicApiService
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel : ViewModel() {

    private val _comicstate = mutableStateOf(ComicsState())
    val comicstate : State<ComicsState> = _comicstate

    init {
        fetchComics()
    }

    private fun fetchComics(){
        viewModelScope.launch {
            try {
                val response = comicApiService.getAllManga()
                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    list = response.data,
                    error = null
                )


            }catch (e : Exception){
                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    error = "error fetching ${e.message}"
                )
            }
        }
    }

    data class ComicsState(
        val loading : Boolean = true,
        val list : List<Comic> = emptyList(),
        val error : String? = null
    )

}