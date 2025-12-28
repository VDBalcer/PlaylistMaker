package com.example.playlistmaker.player.ui.model

sealed class PlayerState(
    val progress: String,
    val isFavorite: Boolean,
) {

    class Default(isFavorite: Boolean) :
        PlayerState("00:00", isFavorite)

    class Prepared(isFavorite: Boolean) :
        PlayerState("00:00", isFavorite)

    class Playing(
        progress: String,
        isFavorite: Boolean,
    ) : PlayerState(progress, isFavorite)

    class Paused(
        progress: String,
        isFavorite: Boolean,
    ) : PlayerState(progress, isFavorite)
}