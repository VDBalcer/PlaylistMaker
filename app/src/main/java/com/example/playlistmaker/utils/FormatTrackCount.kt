package com.example.playlistmaker.utils

fun formatTrackCount(count: Int): String {
    val text = when {
        count % 100 in 11..14 -> "треков"
        count % 10 == 1 -> "трек"
        count % 10 in 2..4 -> "трека"
        else -> "треков"
    }
    return "$count $text"
}