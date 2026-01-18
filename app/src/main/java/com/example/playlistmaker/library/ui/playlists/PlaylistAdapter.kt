package com.example.playlistmaker.library.ui.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistAdapter(
    val onPlaylistClick: (Playlist) -> Unit = {}
) : RecyclerView.Adapter<PlaylistViewHolder>()  {
    var playlists: List<Playlist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaylistViewHolder.from(parent)

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClick(playlists[position]) }
    }
}