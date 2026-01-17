package com.example.playlistmaker.library.ui.new_playlist

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistCreationValidator
import com.example.playlistmaker.library.ui.model.FormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistsInteractor,
    private val formValidator: PlaylistCreationValidator,
) : ViewModel() {

    private val _state =
        MutableLiveData<FormState>(FormState.Default)

    fun observeFormStateState(): LiveData<FormState> = _state


    fun updateState(
        im: Uri = _state.value!!.im,
        name: String = _state.value!!.name,
        description: String = _state.value!!.description,
    ) {
        val isValid = formValidator.isFormValid(
            name = name,
            description = description,
            coverUri = if (im == Uri.EMPTY) null else im
        )

        _state.value = if (isValid) {
            FormState.Valid(im, name, description)
        } else {
            FormState.Invalid(im, name, description)
        }
    }

    fun createPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.addPlaylist(
                playlistName = _state.value!!.name,
                playlistDescription = _state.value!!.description,
                coverIm = _state.value!!.im
            )
        }
    }
}