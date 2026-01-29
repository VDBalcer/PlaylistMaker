package com.example.playlistmaker.library.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int? = null,
    val name: String,
    val description: String = "",
    var coverIm: Uri = Uri.EMPTY,
    var idsList: List<Int> = emptyList(),
    var tracksCount: Int = 0,
    var tracksLength: Int = 0
) : Parcelable
