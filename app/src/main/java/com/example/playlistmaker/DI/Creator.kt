package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTracksRepositoryImpl
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
import com.example.playlistmaker.settings.data.repository.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ResourceProvider
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
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

    private const val historyPrefsStorageKey = "searched_tracks"
    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                context,
                historyPrefsStorageKey,
                object : TypeToken<List<Track>>() {}.type
            )
        )
    }

    private const val themePrefsStorageKey = "app_dark_theme_preferences"
    fun getThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }

    private fun getThemeRepository(context: Context): ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(
            PrefsStorageClient(
                context,
                themePrefsStorageKey,
                object : TypeToken<ThemeSettings>() {}.type
            )
        )
    }

    fun getSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            getResourceProvider(context),
            getExternalNavigator(context)
        )
    }
    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
    private fun getResourceProvider(context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    fun getPlayerInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(trackUrl)
    }

}