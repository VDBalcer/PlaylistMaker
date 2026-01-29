package com.example.playlistmaker.utils

fun String.toSeconds(): Int {
    val delimiterIndex = this.indexOf(':')
    if (delimiterIndex == -1) return 0

    val minutes = this.substring(0, delimiterIndex).toIntOrNull() ?: 0
    val seconds = this.substring(delimiterIndex + 1).toIntOrNull() ?: 0
    return (minutes * 60) + seconds
}