package com.example.playlistmaker.ui.search

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackViewHolder(
    itemView: View,
    val onTrackClick: (Track) -> Unit = {}
) : RecyclerView.ViewHolder(itemView) {
    private var ivArtwork = itemView.findViewById<ImageView>(R.id.track_artwork_image)
    private var tvTrackTitle = itemView.findViewById<TextView>(R.id.track_title)
    private var tvTrackDescriptor = itemView.findViewById<TextView>(R.id.track_description)

    fun bind(item: Track) {
        tvTrackTitle.text = item.trackName

        tvTrackDescriptor.text =
            itemView.context.getString(
                R.string.track_descriptor,
                item.artistName,
                item.trackTime
            )
        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(ivArtwork)

        itemView.setOnClickListener {
            onTrackClick(item)
        }
    }

    val CORNER_RADIUS = 2f
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}