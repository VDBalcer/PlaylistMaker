package com.example.playlistmaker.library.ui.new_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistCreationValidator
import com.example.playlistmaker.library.ui.model.FormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistsInteractor,
    private val formValidator: PlaylistCreationValidator,
) : NewPlaylistViewModel(
    playlistInteractor, formValidator
) {

    private val fillForm = SingleLiveEvent<FormState>()
    fun observeFillingForm(): LiveData<FormState> = fillForm

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                fillForm.postValue(
                    FormState.Valid(
                        im = playlist.coverIm,
                        name = playlist.name,
                        description = playlist.description
                    )
                )
            }
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.updatePlaylist(
                playlistId = playlistId,
                playlistName = _state.value!!.name,
                playlistDescription = _state.value!!.description,
                coverIm = _state.value!!.im
            )
            _closeScreenEvent.postValue(Unit)
        }
    }
}