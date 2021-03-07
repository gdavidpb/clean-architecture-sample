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
) : BaseAdapter<TrackItem>(AdapterComparator.comparator) {

    object AdapterComparator {
        val comparator = compareBy(TrackItem::trackId)
    }

    interface AdapterManager {
        fun onPlayTrackClicked(item: TrackItem)
        fun onPauseTrackClicked(item: TrackItem)
        fun onPreviewTrackClicked(item: TrackItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TrackItem> {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView).also {
            with(itemView) {
                onClickOnce {
                    val item = it.getItem()

                    if (item != null)
                        when {
                            item.isMusic -> {
                                if (item.isPlaying)
                                    manager.onPauseTrackClicked(item)
                                else
                                    manager.onPlayTrackClicked(item)
                            }
                            item.isVideo -> {
                                manager.onPreviewTrackClicked(item)
                            }
                        }
                }
            }
        }
    }

    override fun updateItem(item: TrackItem, update: TrackItem.() -> TrackItem) {
        resetStates()

        super.updateItem(item, update)
    }

    fun resetStates() {
        currentList.mapIndexedNotNull { index, track ->
            if (track.isPlaying || track.isPaused || track.isDownloading)
                index
            else
                null
        }.forEach { index ->
            currentList[index] = currentList[index].copy(
                isPlaying = false,
                isPaused = false,
                isDownloading = false
            )

            notifyItemChanged(index)
        }
    }
}