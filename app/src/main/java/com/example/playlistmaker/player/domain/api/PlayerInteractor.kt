package com.example.playlistmaker.player.domain.api

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun getCurrentPosition(): Int
    fun isTrackPlaying(): Boolean
}