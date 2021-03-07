package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.ui.viewholders.TrackViewHolder
import com.gdavidpb.test.utils.extensions.onClickOnce

class TrackAdapter(
    private val manager: AdapterManager
) : BaseAdapter<TrackItem>() {

    interface AdapterManager {
        fun onPlayTrackClicked(track: TrackItem, position: Int)
        fun onPauseTrackClicked(track: TrackItem, position: Int)
        fun onPreviewTrackClicked(track: TrackItem, position: Int)
    }

    override fun provideComparator() = compareBy(TrackItem::trackId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TrackItem> {
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

    override fun updateItem(position: Int, update: TrackItem.() -> TrackItem) {
        resetStates()

        super.updateItem(position, update)
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