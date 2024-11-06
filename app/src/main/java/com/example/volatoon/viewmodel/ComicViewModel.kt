package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.DetailChapter
import com.example.volatoon.model.DetailComic
import com.example.volatoon.model.Genre
import com.example.volatoon.model.comicApiService
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class ComicViewModel : ViewModel() {

    private val _comicstate = mutableStateOf(ComicsState())
    private val _comicDetailState = mutableStateOf(DetailComicState())
    private val _chapterDetailState = mutableStateOf(DetailChapterState())

    val comicstate : State<ComicsState> = _comicstate
    val detailComicState : State<DetailComicState> = _comicDetailState
    val detailChapterState : State<DetailChapterState> = _chapterDetailState

    init {
        fetchComics()
    }

    fun fetchDetailChapter(chapterId : String){
        _chapterDetailState.value = _chapterDetailState.value.copy(
            loading = true,
        )

        viewModelScope.launch {
            try {
                val response = comicApiService.getDetailChapter(chapterId)

                _chapterDetailState.value = _chapterDetailState.value.copy(
                    loading = false,
                    detailChapter = response,
                    error = null
                )


            }catch (e : Exception){
                _chapterDetailState.value = _chapterDetailState.value.copy(
                    loading = false,
                    error = "error fetching chapter ${e.message}"
                )
            }
        }
    }

    fun fetchDetailComic(comicId : String){
        _comicDetailState.value = _comicDetailState.value.copy(
            loading = true,
        )

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
                    error = "error fetching detail comic ${e.message}"
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
              //  val responseGenre = comicApiService.getAllGenre()

                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    listManga = responseManga.data,
                    listManhua = responseManhua.data,
                    listManhwa = responseManhwa.data,
                   // listGenre = responseGenre.data,
                    error = null
                )


            }catch (e : Exception){
                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    error = "error fetching comic ${e.message}"
                )
            }
        }
    }

    data class ComicsState(
        val loading : Boolean = true,
        val listManga : List<Comic> = emptyList(),
        val listManhua : List<Comic> = emptyList(),
        val listManhwa : List<Comic> = emptyList(),
        //val listGenre : List<Genre> = emptyList(),
        val error : String? = null
    )

    data class DetailComicState(
        val loading : Boolean = true,
        val detailComic : DetailComic? = null,
        val error : String? = null
    )

    data class DetailChapterState(
        val loading : Boolean = true,
        val detailChapter : DetailChapter? = null,
        val error : String? = null
    )

}