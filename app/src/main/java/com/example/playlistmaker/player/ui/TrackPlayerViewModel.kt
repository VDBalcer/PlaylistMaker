package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.FavoriteInteractor
import com.example.playlistmaker.player.ui.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoriteInteractor: FavoriteInteractor,
) : ViewModel() {

    private val playerStateLiveData =
        MutableLiveData<PlayerState>(PlayerState.Default(track.isFavorite))

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var timerJob: Job? = null

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared(playerStateLiveData.value!!.isFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerStateLiveData.postValue(PlayerState.Prepared(playerStateLiveData.value!!.isFavorite))
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.Playing(getCurrentPosition(), playerStateLiveData.value!!.isFavorite))
        startTimerUpdate()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Paused(getCurrentPosition(), playerStateLiveData.value!!.isFavorite))
    }

    fun playButtonClick() {
        when (playerStateLiveData.value) {
            is PlayerState.Paused, is PlayerState.Prepared -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {}
        }
    }

    private fun getCurrentPosition(): String {
        return timerFormat.format(mediaPlayer.currentPosition) ?: "00:00"
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY)
                playerStateLiveData.postValue(
                    PlayerState.Playing(
                        getCurrentPosition(),
                        playerStateLiveData.value!!.isFavorite
                    )
                )
            }
        }
    }

    fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        timerJob?.cancel()
        playerStateLiveData.value = PlayerState.Default(playerStateLiveData.value!!.isFavorite)
    }

    fun onFavoriteClicked() {
        val currentState = playerStateLiveData.value ?: return
        val newIsFavorite = !currentState.isFavorite

        viewModelScope.launch {
            if (newIsFavorite) {
                favoriteInteractor.addTrack(track)
            } else {
                favoriteInteractor.deleteTrack(track)
            }
            playerStateLiveData.value = when (currentState) {
                is PlayerState.Default ->
                    PlayerState.Default(isFavorite = newIsFavorite)

                is PlayerState.Prepared ->
                    PlayerState.Prepared(isFavorite = newIsFavorite)

                is PlayerState.Playing ->
                    PlayerState.Playing(getCurrentPosition(), isFavorite = newIsFavorite)

                is PlayerState.Paused ->
                    PlayerState.Paused(getCurrentPosition(), isFavorite = newIsFavorite)
            }
        }
    }


    companion object {
        const val DELAY = 250L
    }
}