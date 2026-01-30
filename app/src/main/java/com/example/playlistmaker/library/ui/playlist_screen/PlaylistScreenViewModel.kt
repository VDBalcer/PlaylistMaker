package com.example.playlistmaker.library.ui.playlist_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.DI.viewModelModule
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.playlist_screen.model.PlaylistScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private val playlistLiveData = MutableLiveData<PlaylistScreenState>()
    fun observePlaylist(): LiveData<PlaylistScreenState> = playlistLiveData

    fun loadPlaylistInfo(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                val tracks = playlistsInteractor.getTracksByIds(playlist.idsList).first()
                playlistLiveData.postValue(PlaylistScreenState(playlist, tracks))
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(track, playlistLiveData.value!!.playlist)
        }
    }

    fun onSharePlaylistClicked(playlistId: Int) {
        viewModelScope.launch {
            sharingInteractor.sharePlaylist(playlistId)
        }
    }

}