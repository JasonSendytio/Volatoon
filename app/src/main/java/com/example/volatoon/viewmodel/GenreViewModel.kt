package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.comicApiService
import com.example.volatoon.model.Genre
import com.example.volatoon.model.Genres
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {
    private val _genreState = mutableStateOf(GenreState())
    val genreState: State<GenreState> = _genreState

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
                        !listOf("Yaoi", "Yuri", "Doujinshi", "Ecchi").contains(genre.name)
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



    fun fetchComicsByGenre(genreId: String) {
        viewModelScope.launch {
            try {
                _genreState.value = _genreState.value.copy(loading = true)
                val response = comicApiService.getAllGenre(genreId.toInt())
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    genreComics = response.data,
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

    data class GenreState(
        val loading: Boolean = true,
        val listGenres: List<Genres> = emptyList(),
        val genreComics: List<Comic> = emptyList(),
        val error: String? = null
    )
}
