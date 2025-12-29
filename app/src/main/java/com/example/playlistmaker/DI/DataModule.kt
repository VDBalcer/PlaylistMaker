package com.example.playlistmaker.DI

import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.ItunesClient
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.settings.data.storage.StorageClient
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ResourceProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }
    factory<Gson> { Gson() }

    factory<MediaPlayer> { MediaPlayer() }

    single<NetworkClient> {
        ItunesClient(get())
    }

    single<StorageClient<List<Track>>>(qualifier = named(SEARCHED_TRACKS_DATA_KEY)) {
        PrefsStorageClient(
            androidContext(),
            SEARCHED_TRACKS_DATA_KEY,
            object : TypeToken<List<Track>>() {}.type,
            get()
        )
    }

    single<StorageClient<ThemeSettings>>(qualifier = named(THEME_SETTINGS_DATA_KEY)) {
        PrefsStorageClient(
            androidContext(),
            THEME_SETTINGS_DATA_KEY,
            object : TypeToken<ThemeSettings>() {}.type,
            get()
        )
    }

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }
    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

}

const val SEARCHED_TRACKS_DATA_KEY = "searched_tracks"
const val THEME_SETTINGS_DATA_KEY = "theme_settings"