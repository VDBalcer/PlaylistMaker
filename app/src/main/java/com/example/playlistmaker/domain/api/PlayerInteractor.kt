package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun getCurrentPosition(): Int
    fun isTrackPlaying(): Boolean
}