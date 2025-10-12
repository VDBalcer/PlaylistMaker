package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class TracksSearchResponse(
    @SerializedName("results") val results: ArrayList<TrackDto>,
    @SerializedName("resultCount") val resultCount: Int
) : Response()