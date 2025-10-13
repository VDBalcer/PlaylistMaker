package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerViewModel(
    trackUrl: String
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData(STATE_DEFAULT)
    fun observePlayerState(): LiveData<Int> = playerStateLiveData

    private val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val progressTimeLiveData = MutableLiveData(timerFormat.format(0))
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData.value == STATE_PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(STATE_PREPARED)
            resetTimer()
        }
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        progressTimeLiveData.postValue(timerFormat.format(0))
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(STATE_PLAYING)
        startTimerUpdate()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerStateLiveData.postValue(STATE_PAUSED)
        timerRunnable.let { handler.removeCallbacks(it) }
    }

    fun playButtonClick() {
        when (playerStateLiveData.value) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun getCurrentPosition(): Int {
        return when (playerStateLiveData.value) {
            STATE_DEFAULT, STATE_PREPARED -> 0
            else -> mediaPlayer.currentPosition
        }
    }

    private fun startTimerUpdate() {
        progressTimeLiveData.postValue(
            timerFormat.format(getCurrentPosition())
        )
        handler.postDelayed(timerRunnable, DELAY)
    }

    fun releasePlayer() {
        timerRunnable.let { handler.removeCallbacks(it) }
        playerStateLiveData.postValue(STATE_DEFAULT)
        mediaPlayer.release()
    }

    companion object {
        const val DELAY = 250L

        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        fun getFactory(
            track: Track
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackPlayerViewModel(
                    track.previewUrl
                )
            }
        }
    }
}