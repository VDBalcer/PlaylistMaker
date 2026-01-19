package com.example.playlistmaker.library.domain

import android.net.Uri

class PlaylistCreationValidator {

    fun isFormValid(
        name: String,
        description: String,
        coverUri: Uri?
    ): Boolean {
        val trimmedName = name.trim()
        return trimmedName.isNotEmpty()
    }
}