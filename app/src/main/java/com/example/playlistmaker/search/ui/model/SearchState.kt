package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchState {
    data object Loading : SearchState

    data object Error : SearchState

    data object Empty : SearchState

    data class Search(
        val tracks: List<Track>
    ) : SearchState

    data class History(
        val tracks: List<Track>
    ) : SearchState
}