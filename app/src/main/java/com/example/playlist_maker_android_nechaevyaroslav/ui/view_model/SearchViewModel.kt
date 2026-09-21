package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_android_nechaevyaroslav.MyApplication
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
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
        viewModelScope.launch {
            _searchScreenState.value = SearchState.Searching
            try {
                val result = trackRepository.searchTracksContains(query)
                if (result.isNotEmpty()) {
                    _searchScreenState.value = SearchState.Success(result)
                } else {
                    _searchScreenState.value = SearchState.Fail("Ничего не найдено")
                }
            } catch (e: Exception) {
                _searchScreenState.value = SearchState.Fail("Ошибка при поиске: ${e.message}")
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.value = SearchState.Initial
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as MyApplication).getTracksRepository()
                SearchViewModel(repository)
            }
        }
    }
}
