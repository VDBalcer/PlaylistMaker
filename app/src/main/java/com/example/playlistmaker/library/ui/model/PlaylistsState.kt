package com.example.playlistmaker.library.ui.model

import com.example.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistsState {
    data object Loading : PlaylistsState

    data object Empty : PlaylistsState

    data class Playlists(
        val playlists: List<Playlist>,
    ) : PlaylistsState
}