package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.drawables
import com.gdavidpb.test.utils.formatInterval
import com.gdavidpb.test.utils.onClickOnce
import kotlinx.android.synthetic.main.item_track.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource

open class TrackViewHolder(
    itemView: View,
    private val callback: TrackAdapter.AdapterCallback
) : BaseViewHolder<Track>(itemView) {
    override fun bindView(item: Track) {
        with(itemView) {
            tViewTrackName.text = item.trackName
            tViewTrackName.drawables(right = if (item.isTrackExplicit) R.drawable.ic_explicit else 0)
            tViewTrackTime.text = item.trackTimeMillis.formatInterval()

            when {
                item.isPlaying -> {
                    tViewTrackName.textColorResource = R.color.colorAccent
                    tViewTrackTime.textColorResource = R.color.colorAccent
                    iViewTrackAction.imageResource = R.drawable.ic_pause

                    pBarTrack.visibility = View.GONE
                    iViewTrackAction.visibility = View.VISIBLE
                }
                item.isPaused -> {
                    tViewTrackName.textColorResource = R.color.colorAccent
                    tViewTrackTime.textColorResource = R.color.colorAccent
                    iViewTrackAction.imageResource = R.drawable.ic_play_on

                    pBarTrack.visibility = View.GONE
                    iViewTrackAction.visibility = View.VISIBLE
                }
                item.isDownloading -> {
                    tViewTrackName.textColorResource = R.color.colorAccent
                    tViewTrackTime.textColorResource = R.color.colorAccent

                    iViewTrackAction.visibility = View.INVISIBLE
                    pBarTrack.visibility = View.VISIBLE
                }
                else -> {
                    when {
                        item.isMusic -> {
                            tViewTrackName.textColorResource = R.color.colorForegroundDark
                            tViewTrackTime.textColorResource = R.color.colorForegroundDark
                            iViewTrackAction.imageResource = R.drawable.ic_play

                            pBarTrack.visibility = View.GONE
                            iViewTrackAction.visibility = View.VISIBLE
                        }
                        item.isVideo -> {
                            iViewTrackAction.imageResource = R.drawable.ic_video
                        }
                    }
                }
            }

            onClickOnce {
                val updatedItem = callback.getTrack(adapterPosition)

                when {
                    updatedItem.isMusic -> {
                        if (updatedItem.isPlaying)
                            callback.onPauseTrackClicked(track = updatedItem, position = adapterPosition)
                        else
                            callback.onPlayTrackClicked(track = updatedItem, position = adapterPosition)
                    }
                    updatedItem.isVideo -> {
                        callback.onPreviewTrackClicked(track = updatedItem, position = adapterPosition)
                    }
                }
            }
        }
    }
}