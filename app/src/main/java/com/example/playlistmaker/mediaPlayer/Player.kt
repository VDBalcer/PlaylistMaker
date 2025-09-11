package com.example.playlistmaker.mediaPlayer

import android.media.MediaPlayer
import android.widget.ImageButton
import com.example.playlistmaker.R

class Player(val trackUrl: String, val playButton: ImageButton) {
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_stop)
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun release() {
        playerState = STATE_DEFAULT
        mediaPlayer.release()
    }

    fun getCurrentPosition(): Int {
        return when (playerState) {
            STATE_DEFAULT, STATE_PREPARED -> 0
            else -> mediaPlayer.currentPosition
        }
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}