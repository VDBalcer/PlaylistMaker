package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.DI.Creator
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    companion object {
        const val DELAY = 250L
        const val PLAYING = true
        const val STOPPED = false

        fun getFactory(
            track: Track
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackPlayerViewModel(
                    Creator.getPlayerInteractor(track.previewUrl)
                )
            }
        }
    }

    private val isPlayingStateLiveData = MutableLiveData(STOPPED)
    fun observePlayerState(): LiveData<Boolean> = isPlayingStateLiveData

    private val progressTimeLiveData = MutableLiveData("00:00")
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (isPlayingStateLiveData.value == true) {
            startTimerUpdate()
        }
    }

    private fun startTimerUpdate() {
        progressTimeLiveData.postValue(
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                playerInteractor.getCurrentPosition()
            )
        )
        handler.postDelayed(timerRunnable, DELAY)
    }

    fun playButtonClick() {
        playerInteractor.playbackControl()
        if (playerInteractor.isTrackPlaying()) {
            isPlayingStateLiveData.postValue(PLAYING)
            handler.post(timerRunnable)
        } else {
            isPlayingStateLiveData.postValue(STOPPED)
            timerRunnable.let { handler.removeCallbacks(it) }
        }

    }

    fun pausePlayer(){
        playerInteractor.pausePlayer()
        isPlayingStateLiveData.postValue(STOPPED)
        timerRunnable.let { handler.removeCallbacks(it) }
    }

    fun releasePlayer(){
        timerRunnable.let { handler.removeCallbacks(it) }
        playerInteractor.release()
    }
}