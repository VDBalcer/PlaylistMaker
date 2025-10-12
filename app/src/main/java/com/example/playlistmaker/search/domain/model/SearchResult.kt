package com.example.playlistmaker.search.domain.model

sealed class SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult()
    data object NetworkError : SearchResult()
    data class ServerError(val code: Int) : SearchResult()
}