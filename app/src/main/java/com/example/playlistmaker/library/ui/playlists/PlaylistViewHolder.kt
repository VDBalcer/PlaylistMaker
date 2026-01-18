package com.example.playlistmaker.library.ui.playlists

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding,
    val onPlaylistClick: (Playlist) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist){
        binding.playlistTitle.text = item.name
        binding.playlistTracksCount.text = item.tracksCount.toString()

        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)
        Glide.with(itemView)
            .load(item.coverIm)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.playlistCoverIm)
    }
    
    companion object {
        fun from(parent: ViewGroup): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaylistBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding)
        }
        val CORNER_RADIUS = 2f
    }
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}