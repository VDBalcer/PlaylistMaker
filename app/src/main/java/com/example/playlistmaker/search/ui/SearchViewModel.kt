package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    var searchDebounce = debounce(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query: String ->
        if (query.isBlank()) {
            showHistory()
            return@debounce
        }
        stateLiveData.postValue(SearchState.Loading)
        searchTracks(query)
    }

    private fun searchTracks(query: String) {
        viewModelScope.launch {
            searchTracksInteractor
                .searchTracks(query)
                .collect { searchResult ->
                    when (searchResult) {
                        is SearchResult.Success -> {
                            if (searchResult.tracks.isEmpty()) {
                                stateLiveData.postValue(SearchState.Empty)
                            } else {
                                stateLiveData.postValue(SearchState.Search(searchResult.tracks))
                            }
                        }

                        else -> stateLiveData.postValue(SearchState.Error)
                    }
                }
        }
    }

    fun showHistory() {
        viewModelScope.launch {
            searchHistoryInteractor.getTracksHistory(object :
                SearchHistoryInteractor.HistoryConsumer {
                override fun consume(historyTracks: List<Track>) {
                    stateLiveData.postValue(SearchState.History(historyTracks))
                }
            })
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            searchHistoryInteractor.clearHistory()
            stateLiveData.postValue(SearchState.History(emptyList()))
        }
    }

    fun onTrackClicked(clickedTrack: Track) {
        viewModelScope.launch {
            searchHistoryInteractor.addTrackToHistory(clickedTrack)
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}