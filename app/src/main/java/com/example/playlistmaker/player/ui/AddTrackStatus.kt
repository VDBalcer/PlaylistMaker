package com.example.playlistmaker.player.ui

sealed class AddTrackStatus {
    data class Added(val playlistName: String) : AddTrackStatus()
    data class AlreadyExists(val playlistName: String) : AddTrackStatus()
}
