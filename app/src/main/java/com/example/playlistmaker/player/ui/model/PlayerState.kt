package com.example.playlistmaker.player.ui.model

sealed class PlayerState(val progress: String) {

    class Default : PlayerState("00:00")

    class   Prepared : PlayerState("00:00")

    class Playing(progress: String) : PlayerState(progress)

    class Paused(progress: String) : PlayerState(progress)
}