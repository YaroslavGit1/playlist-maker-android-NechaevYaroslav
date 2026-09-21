package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState: StateFlow<SearchState> = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        performSearch(query)
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        searchJob?.cancel()
        _searchQuery.value = ""
        _searchScreenState.value = SearchState.Initial
    }

    suspend fun getHistoryList(): List<String> = searchHistoryRepository.getHistory()

    private fun performSearch(request: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.value = SearchState.Searching
            try {
                searchHistoryRepository.addToHistory(request)
                val foundTracks = tracksRepository.searchTracksContains(request)
                _searchScreenState.value = SearchState.Success(foundTracks)
            } catch (e: IOException) {
                _searchScreenState.value = SearchState.Fail(e.message.toString())
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 1000L
    }
}
