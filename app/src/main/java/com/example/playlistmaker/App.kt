package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(APP_THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun changeTheme(checked: Boolean) {
        val sharedPrefs = getSharedPreferences(APP_THEME_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, checked)
            .apply()
        (applicationContext as App).switchTheme(checked)
    }

    companion object {
        const val APP_THEME_PREFERENCES = "app_theme_preferences"
        const val DARK_THEME_KEY = "dark_theme"
    }
}