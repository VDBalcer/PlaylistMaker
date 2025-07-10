package com.example.playlistmaker.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.TrackModel

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var ivArtwork = itemView.findViewById<ImageView>(R.id.track_artwork_image)
    private var tvTrackTitle = itemView.findViewById<TextView>(R.id.track_title)
    private var tvTrackDescriptor = itemView.findViewById<TextView>(R.id.track_description)

    fun bind(item: TrackModel) {
        tvTrackTitle.text = item.trackName

        tvTrackDescriptor.text =
            itemView.context.getString(
                R.string.track_descriptor,
                item.artistName,
                item.trackTime
            )

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(ivArtwork)
    }
}