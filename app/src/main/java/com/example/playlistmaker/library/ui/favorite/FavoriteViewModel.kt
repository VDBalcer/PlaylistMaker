package com.example.playlistmaker.library.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.FavoriteInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {
    val stateLiveData = MutableLiveData<FavoriteState>(FavoriteState.Loading)
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillFavourite() {
        stateLiveData.postValue(FavoriteState.Loading)
        viewModelScope.launch {
            favoriteInteractor.favoriteTracks().collect{ tracks ->
                if (tracks.isEmpty()) {
                    stateLiveData.postValue(FavoriteState.Empty)
                } else {
                    stateLiveData.postValue(FavoriteState.Favorite(tracks))
                }
            }
        }
    }

    sealed interface FavoriteState {
        data object Loading : FavoriteState

        data object Empty : FavoriteState

        data class Favorite(
            val tracks: List<Track>
        ) : FavoriteState
    }
}