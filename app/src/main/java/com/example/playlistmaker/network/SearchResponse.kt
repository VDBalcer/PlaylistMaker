package com.example.playlistmaker.network

import com.example.playlistmaker.data.TrackDto
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("results") val results : ArrayList<TrackDto>,
    @SerializedName("resultCount") val resultCount : Int
)