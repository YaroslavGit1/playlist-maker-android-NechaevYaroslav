package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackRepository: TracksRepository,
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState: StateFlow<SearchState> = _searchScreenState.asStateFlow()

    fun searchTracks(query: String) {
        val expression = query.trim()
        if (expression.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.value = SearchState.Searching
            try {
                val result = trackRepository.searchTracksContains(expression)
                _searchScreenState.value = SearchState.Success(result)
            } catch (e: Exception) {
                _searchScreenState.value = SearchState.Fail(e.message.toString())
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.value = SearchState.Initial
    }
}
