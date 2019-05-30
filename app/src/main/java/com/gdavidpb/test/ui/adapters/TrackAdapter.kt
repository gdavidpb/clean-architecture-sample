package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.ui.viewholders.TrackViewHolder
import com.gdavidpb.test.utils.onClickOnce

open class TrackAdapter(
    private val manager: AdapterManager
) : BaseAdapter<Track>() {

    interface AdapterManager {
        fun onPlayTrackClicked(track: Track, position: Int)
        fun onPauseTrackClicked(track: Track, position: Int)
        fun onPreviewTrackClicked(track: Track, position: Int)
    }

    override fun provideComparator() = compareBy(Track::trackId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Track> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView).also {
            with(itemView) {
                onClickOnce {
                    val item = it.resolveItem()

                    if (item != null)
                        when {
                            item.isMusic -> {
                                if (item.isPlaying)
                                    manager.onPauseTrackClicked(track = item, position = it.adapterPosition)
                                else
                                    manager.onPlayTrackClicked(track = item, position = it.adapterPosition)
                            }
                            item.isVideo -> {
                                manager.onPreviewTrackClicked(track = item, position = it.adapterPosition)
                            }
                        }
                }
            }
        }
    }

    fun updateItem(position: Int, update: Track.() -> Track) {
        resetStates()

        items[position] = items[position].update()

        notifyItemChanged(position)
    }

    fun updateItem(item: Track, update: Track.() -> Track) {
        val position = items.indexOf(item)

        updateItem(position, update)
    }

    fun resetStates() {
        items.mapIndexedNotNull { index, track ->
            if (track.isPlaying || track.isPaused || track.isDownloading)
                index
            else
                null
        }.forEach { index ->
            items[index] = items[index].copy(
                isPlaying = false,
                isPaused = false,
                isDownloading = false
            )

            notifyItemChanged(index)
        }
    }
}