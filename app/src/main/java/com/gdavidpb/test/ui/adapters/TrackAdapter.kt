package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.ui.viewholders.TrackViewHolder

class TrackAdapter(
    private val manager: AdapterManager
) : BaseAdapter<TrackItem>(AdapterComparator.comparator) {

    init {
        setHasStableIds(true)
    }

    object AdapterComparator {
        val comparator = compareBy(TrackItem::trackId)
    }

    interface AdapterManager {
        fun onPlayTrackClicked(item: TrackItem)
        fun onPauseTrackClicked(item: TrackItem)
        fun onPreviewTrackClicked(item: TrackItem)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).trackId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TrackItem> {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView, manager)
    }

    override fun updateItem(item: TrackItem, update: TrackItem.() -> TrackItem) {
        resetStates()

        super.updateItem(item, update)
    }

    fun resetStates() {
        currentList.mapIndexedNotNull { index, track ->
            if (track.isPlaying || track.isPaused || track.isLoading)
                index
            else
                null
        }.forEach { index ->
            currentList[index] = currentList[index].copy(
                    isPlaying = false,
                    isPaused = false,
                    isLoading = false
            )

            notifyItemChanged(index)
        }
    }
}