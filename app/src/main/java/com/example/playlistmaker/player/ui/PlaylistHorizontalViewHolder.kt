package com.example.playlistmaker.player.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistHorizontalBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.utils.formatTrackCount

class PlaylistHorizontalViewHolder(
    private val binding: ItemPlaylistHorizontalBinding,
    val onPlaylistClick: (Playlist) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist){
        binding.playlistTitle.text = item.name
        binding.playlistDescription.text = formatTrackCount(item.tracksCount)

        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)
        Glide.with(itemView)
            .load(item.coverIm)
            .placeholder(R.drawable.track_placeholder)
            .centerInside()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.playlistCoverIm)
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistHorizontalViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaylistHorizontalBinding.inflate(inflater, parent, false)
            return PlaylistHorizontalViewHolder(binding)
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