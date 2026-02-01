package com.example.playlistmaker.library.ui.playlist_screen.model

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.toSeconds

class PlaylistScreenState(
    val playlist: Playlist,
    val tracks: List<Track>,
) {
    val totalDurationSeconds: Int = tracks.sumOf { it.trackTime.toSeconds() }
}
