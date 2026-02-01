package com.example.playlistmaker.DI

import com.example.playlistmaker.db.domain.FavoriteInteractor
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.db.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker.db.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.library.domain.PlaylistCreationValidator
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get(), get())
    }
    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get(), get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get(), get())
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    factory<PlaylistCreationValidator> { PlaylistCreationValidator() }
}