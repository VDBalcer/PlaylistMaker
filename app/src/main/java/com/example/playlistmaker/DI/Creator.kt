package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTracksRepositoryImpl
import com.example.playlistmaker.settings.data.dto.repository.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.search.data.network.ItunesClient
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.settings.data.storage.ThemeStorageClient
import com.google.gson.reflect.TypeToken

object Creator {

    fun getTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(ItunesClient())
    }

    fun getHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                context,
                object : TypeToken<List<Track>>() {}.type
            )
        )
    }

    fun getThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }
    private fun getThemeRepository(context: Context): ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(ThemeStorageClient(context))
    }

    fun getPlayerInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(trackUrl)
    }
}