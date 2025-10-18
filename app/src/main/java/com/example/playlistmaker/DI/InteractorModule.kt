package com.example.playlistmaker.DI

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

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }
}