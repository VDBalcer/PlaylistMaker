package com.example.playlistmaker.model

data class Track(
    val trackId : Int, // id композиции
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)
