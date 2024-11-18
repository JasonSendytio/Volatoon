package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.comicApiService
import com.example.volatoon.viewmodel.ComicViewModel.DetailComicState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.debounce
import java.lang.Exception

class SearchViewModel : ViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val comics = _searchText
        .debounce(300) // add debounce for delay between user input
        .flatMapLatest { text ->
            if (text.isBlank()) {
                // Return an empty comic list if the search text is blank
                flowOf(ComicsState())
            } else {
                // Perform search when text is entered
                searchComic(text)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ComicsState(loading = true)
        )

    private fun searchComic(query: String): Flow<ComicsState> = flow {
        try {
            // Indicate loading state while fetching data
            emit(ComicsState(loading = true))

            // Perform the API call to search comics
            val responseFilter = comicApiService.searchComic(query)

            // Emit the result once data is fetched
            emit(ComicsState(loading = false, listComic = responseFilter.data, error = null))
        } catch (e: Exception) {
            // Handle error state in case of an exception
            emit(ComicsState(loading = false, listComic = emptyList(), error = e.message))
        }
    }

    fun onSearchTextChange(text: String) {
        if(text == "") _isSearching.value = false
        else _isSearching.value = true

        _searchText.value = text
    }

    fun onClearSearch() {
        _isSearching.value = false
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    data class ComicsState(
        val loading : Boolean = false,
        val listComic : List<Comic> = emptyList(),
        val error : String? = null
    )
}