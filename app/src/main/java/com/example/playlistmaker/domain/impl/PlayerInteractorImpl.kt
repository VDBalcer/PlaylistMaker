package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl(trackUrl: String) : PlayerInteractor {
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun release() {
        playerState = STATE_DEFAULT
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return when (playerState) {
            STATE_DEFAULT, STATE_PREPARED -> 0
            else -> mediaPlayer.currentPosition
        }
    }

    override fun isTrackPlaying(): Boolean {
        when (playerState) {
            STATE_PLAYING -> {
                return true
            }

            STATE_PREPARED, STATE_PAUSED -> {
                return false
            }
        }
        return false
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}