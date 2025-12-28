package com.example.playlistmaker.DI

import com.example.playlistmaker.library.ui.favorite.FavoriteViewModel
import com.example.playlistmaker.library.ui.playlists.PlaylistsViewModel
import com.example.playlistmaker.player.ui.TrackPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        TrackPlayerViewModel(track, get(), get())
    }

    viewModel { _ ->
        FavoriteViewModel(get())
    }

    viewModel { _ ->
        PlaylistsViewModel()
    }
}