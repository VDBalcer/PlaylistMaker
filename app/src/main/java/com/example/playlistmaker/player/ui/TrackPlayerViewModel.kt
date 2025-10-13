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

    private val isPlayingStateLiveData = MutableLiveData(false)
    fun observePlayerState(): LiveData<Boolean> = isPlayingStateLiveData

    private val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val progressTimeLiveData = MutableLiveData(timerFormat.format(0))
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (isPlayingStateLiveData.value == true) {
            startTimerUpdate()
        }
    }

    private fun startTimerUpdate() {
        progressTimeLiveData.postValue(
            timerFormat.format(playerInteractor.getCurrentPosition())
        )
        handler.postDelayed(timerRunnable, DELAY)
    }

    fun playButtonClick() {
        playerInteractor.playbackControl()
        if (playerInteractor.isTrackPlaying()) {
            isPlayingStateLiveData.postValue(true)
            handler.post(timerRunnable)
        } else {
            isPlayingStateLiveData.postValue(false)
            timerRunnable.let { handler.removeCallbacks(it) }
        }

    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        isPlayingStateLiveData.postValue(false)
        timerRunnable.let { handler.removeCallbacks(it) }
    }

    fun releasePlayer() {
        timerRunnable.let { handler.removeCallbacks(it) }
        playerInteractor.release()
    }

    companion object {
        const val DELAY = 250L

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
}