package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.ComicApiService
import com.example.volatoon.model.DetailComic
import com.example.volatoon.model.comicApiService
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel : ViewModel() {

    private val _comicstate = mutableStateOf(ComicsState())
    private val _comicDetailState = mutableStateOf(DetailComicState())

    val comicstate : State<ComicsState> = _comicstate
    val detailComicState : State<DetailComicState> = _comicDetailState

    init {
        fetchComics()
    }

    fun fetchDetailComic(comicId : String){
        viewModelScope.launch {
            try {
                val response = comicApiService.getDetailComic(comicId)

                _comicDetailState.value = _comicDetailState.value.copy(
                    loading = false,
                    detailComic = response,
                    error = null
                )


            }catch (e : Exception){
                _comicDetailState.value = _comicDetailState.value.copy(
                    loading = false,
                    error = "error fetching ${e.message}"
                )
            }
        }
    }

    private fun fetchComics(){
        viewModelScope.launch {
            try {
                val responseManga = comicApiService.getAllManga()
                val responseManhua = comicApiService.getAllManhua()
                val responseManhwa = comicApiService.getAllManhwa()

                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    listManga = responseManga.data,
                    listManhua = responseManhua.data,
                    listManhwa = responseManhwa.data,
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
        val listManga : List<Comic> = emptyList(),
        val listManhua : List<Comic> = emptyList(),
        val listManhwa : List<Comic> = emptyList(),
        val error : String? = null
    )

    data class DetailComicState(
        val loading : Boolean = true,
        val detailComic : DetailComic? = null,
        val error : String? = null
    )

}