package com.example.playlistmaker.library.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.model.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistsInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observeState(): LiveData<PlaylistsState> = _state

    fun getPlaylists() {
        viewModelScope.launch {
            _state.postValue(PlaylistsState.Loading)
            playlistInteractor
                .playlists()
                .collect { result ->
                    if (result.isNotEmpty()) _state.postValue(PlaylistsState.Playlists(result))
                    else _state.postValue(PlaylistsState.Empty)
                }
        }
    }
}