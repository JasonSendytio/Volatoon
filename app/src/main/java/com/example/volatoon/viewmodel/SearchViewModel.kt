package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Comic
import com.example.volatoon.model.RecentSearch
import com.example.volatoon.model.SearchDatabase
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

class SearchViewModel(private val searchDatabase: SearchDatabase) : ViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val TAG = "SearchViewModel"


    private val _recentSearches = MutableStateFlow<List<RecentSearch>>(emptyList())
    val recentSearches = _recentSearches.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        loadRecentSearches()
        Log.d(TAG, "ViewModel initialized")
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val comics = _searchText
        .debounce(300)
        .flatMapLatest { text ->
            Log.d(TAG, "Search text changed: $text")
            if (text.isBlank()) {
                Log.d(TAG, "Empty search, returning empty state")
                flowOf(ComicsState())
            } else {
                Log.d(TAG, "Performing search for: $text")
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
            Log.d(TAG, "Starting search for query: $query")
            emit(ComicsState(loading = true))

            val responseFilter = comicApiService.searchComic(query)
            Log.d(TAG, "Search results received: ${responseFilter.data.size} items")

            emit(ComicsState(loading = false, listComic = responseFilter.data, error = null))
        } catch (e: Exception) {
            Log.e(TAG, "Search error: ${e.message}")
            emit(ComicsState(loading = false, listComic = emptyList(), error = e.message))
        }
    }

    fun onSearchTextChange(text: String) {
        Log.d(TAG, "Search text changing to: $text")
        if(text == "") _isSearching.value = false
        _isSearching.value = text.isNotEmpty()
        _isSearching.value = true

        _searchText.value = text
    }

    fun onClearSearch() {
        Log.d(TAG, "Clear text")

        _isSearching.value = false
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }
    private fun loadRecentSearches() {
        viewModelScope.launch {
            _recentSearches.value = searchDatabase.getRecentSearches()
        }
    }

    fun addRecentSearch(searchText: String) {
        viewModelScope.launch {
            searchDatabase.addSearch(searchText)
            loadRecentSearches()
        }
    }

    fun deleteRecentSearch(searchText: String) {
        viewModelScope.launch {
            searchDatabase.deleteSearch(searchText)
            loadRecentSearches()
        }
    }
    data class ComicsState(
        val loading : Boolean = false,
        val listComic : List<Comic> = emptyList(),
        val error : String? = null
    )
}