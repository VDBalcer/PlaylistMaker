package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.search.domain.model.Track

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L

        fun getFactory(
            searchTracksInteractor: SearchTracksInteractor,
            searchHistoryInteractor: SearchHistoryInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchTracksInteractor = searchTracksInteractor,
                    searchHistoryInteractor = searchHistoryInteractor
                )
            }
        }
    }

    val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private var isTrackClickAllowed = true

    fun searchDebounce(query: String) {
        searchRunnable?.let { handler.removeCallbacks(it) }
        searchRunnable = Runnable { performSearch(query) }
        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            showHistory()
            return
        }

        stateLiveData.postValue(SearchState.Loading)
        searchTracksInteractor.searchTracks(query, object : SearchTracksInteractor.TracksConsumer {
            override fun consume(searchResult: SearchResult) {
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
        })
    }

    fun showHistory() {
        searchHistoryInteractor.getTracksHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(historyTracks: List<Track>) {
                stateLiveData.postValue(SearchState.History(historyTracks))
            }
        })
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        stateLiveData.postValue(SearchState.History(emptyList()))
    }

    fun onTrackClicked(clickedTrack: Track): Boolean {
        searchHistoryInteractor.addTrackToHistory(clickedTrack)
        val current = isTrackClickAllowed
        if (isTrackClickAllowed) {
            isTrackClickAllowed = false
            handler.postDelayed({ isTrackClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }
}