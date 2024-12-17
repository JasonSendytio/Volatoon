package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.comicApiService
import com.example.volatoon.model.Genres
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {
    private val _genreState = mutableStateOf(GenreState())
    val genreState: State<GenreState> = _genreState
    private var currentGenreId: String = ""

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            try {
                val response = comicApiService.getAllGenres()
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    listGenres = response.filter { genre ->
                        !listOf("Adult", "Yaoi", "Yuri", "Doujinshi", "Ecchi").contains(genre.name)
                    },
                    error = null
                )
            } catch (e: Exception) {
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    error = "Error fetching genres: ${e.message}"
                )
            }
        }
    }

    fun fetchComicsByGenre(genreId: String, page: Int = 1) {
        currentGenreId = genreId
        viewModelScope.launch {
            try {
                _genreState.value = _genreState.value.copy(loading = true)
                val response = comicApiService.getAllGenre(
                    genreId = genreId.toIntOrNull() ?: return@launch,
                    page = page
                )
                val currentGenre = _genreState.value.listGenres.find { it.genre_id == genreId }
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    genreComics = response.data,
                    currentGenre = currentGenre,
                    currentPage = page,
                    hasNextPage = response.nextPage,
                    hasPreviousPage = response.prevPage,
                    error = null
                )
            } catch (e: Exception) {
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    error = "Error fetching comics: ${e.message}"
                )
            }
        }
    }

    fun loadNextPage() {
        if (_genreState.value.hasNextPage) {
            fetchComicsByGenre(currentGenreId, _genreState.value.currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        if (_genreState.value.hasPreviousPage) {
            fetchComicsByGenre(currentGenreId, _genreState.value.currentPage - 1)
        }
    }

    data class GenreState(
        val loading: Boolean = true,
        val listGenres: List<Genres> = emptyList(),
        val genreComics: List<Comic> = emptyList(),
        val currentGenre: Genres? = null,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
        val hasPreviousPage: Boolean = false,
        val error: String? = null
    )
}
