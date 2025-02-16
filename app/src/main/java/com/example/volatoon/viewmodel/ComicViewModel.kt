package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.DetailChapter
import com.example.volatoon.model.DetailComic
import com.example.volatoon.model.comicApiService
import kotlinx.coroutines.launch
import java.lang.Exception
import android.util.Log

class ComicViewModel : ViewModel() {

    private val _comicstate = mutableStateOf(ComicsState())
    private val _comicDetailState = mutableStateOf(DetailComicState())
    private val _chapterDetailState = mutableStateOf(DetailChapterState())
    private val _trendingComicsState = mutableStateOf(ComicsState())

    val comicstate : State<ComicsState> = _comicstate
    val trendingComicsState: State<ComicsState> = _trendingComicsState

    val detailComicState : State<DetailComicState> = _comicDetailState
    val detailChapterState : State<DetailChapterState> = _chapterDetailState

    private val _selectedComicId = mutableStateOf<String?>(null)
    val selectedComicId: State<String?> = _selectedComicId

    init {
        fetchComics()
        fetchTrendingComics()
    }

    fun fetchDetailChapter(chapterId : String){
        _chapterDetailState.value = _chapterDetailState.value.copy(
            loading = true,
        )

        viewModelScope.launch {
            try {
                val response = comicApiService.getDetailChapter(chapterId)
                val detailChapter = DetailChapter(
                    title = response.title,
                    komik_id = response.komik_id,
                    chapter_id = chapterId,
                    prev_chapter_id = response.prev_chapter_id ?: "",
                    next_chapter_id = response.next_chapter_id ?: "",
                    images = response.images
                )
                _chapterDetailState.value = _chapterDetailState.value.copy(
                    loading = false,
                    detailChapter = detailChapter,
                    error = null
                )

                    Log.d("ComicViewModel", "Detail Chapter: $detailChapter")


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

    private fun fetchTrendingComics() {
        viewModelScope.launch {
            try {
                val responseManga = comicApiService.getAllManga(order = "popular")
                val responseManhua = comicApiService.getAllManhua(order = "popular")
                val responseManhwa = comicApiService.getAllManhwa(order = "popular")

                _trendingComicsState.value = _trendingComicsState.value.copy(
                    loading = false,
                    listManga = responseManga.data,
                    listManhua = responseManhua.data,
                    listManhwa = responseManhwa.data,
                    error = null
                )
            } catch (e: Exception) {
                _trendingComicsState.value = _trendingComicsState.value.copy(
                    loading = false,
                    error = "error fetching comic ${e.message}"
                )
            }
        }
    }

    private fun fetchComics(){
        viewModelScope.launch {
            try {
                val responseManga = comicApiService.getAllManga(order = "update")
                val responseManhua = comicApiService.getAllManhua(order = "update")
                val responseManhwa = comicApiService.getAllManhwa(order = "update")

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
                    error = "error fetching comic ${e.message}"
                )
            }
        }
    }

    fun fetchMoreComics(type: String, page: Int = 1, order: String = "update") {
        viewModelScope.launch {
            try {
                _comicstate.value = _comicstate.value.copy(loading = true)
                val response = when (type.lowercase()) {
                    "manga" -> comicApiService.getAllManga(page, order)
                    "manhua" -> comicApiService.getAllManhua(page, order)
                    "manhwa" -> comicApiService.getAllManhwa(page, order)
                    else -> throw Exception("Invalid comic type")
                }

                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    currentPageComics = response.data,
                    currentPage = page,
                    hasNextPage = response.nextPage,
                    hasPreviousPage = response.prevPage,
                    error = null
                )
            } catch (e: Exception) {
                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    error = "Error fetching comics: ${e.message}"
                )
            }
        }
    }

    fun refreshComics() {
        viewModelScope.launch {
            try {
                fetchComics()
            } catch (e: Exception) {
                _comicstate.value = _comicstate.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    fun setSelectedComicId(comicId: String){
        _selectedComicId.value = comicId
    }

    data class ComicsState(
        val loading : Boolean = true,
        val listManga : List<Comic> = emptyList(),
        val listManhua : List<Comic> = emptyList(),
        val listManhwa : List<Comic> = emptyList(),
        val currentPageComics: List<Comic> = emptyList(),
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
        val hasPreviousPage: Boolean = false,
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