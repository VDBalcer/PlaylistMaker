package com.example.playlistmaker.search.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.search.domain.model.Track

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    val onTrackClick: (Track) -> Unit = {}
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        binding.trackTitle.text = item.trackName

        binding.trackDescription.text =
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
            .into(binding.trackArtworkImage)

        itemView.setOnClickListener {
            onTrackClick(item)
        }
    }

    val CORNER_RADIUS = 2f

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTrackBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}