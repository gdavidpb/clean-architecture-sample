package cl.yapo.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import cl.yapo.test.R
import cl.yapo.test.domain.model.Track
import cl.yapo.test.ui.viewholders.BaseViewHolder
import cl.yapo.test.ui.viewholders.TrackViewHolder

open class TrackAdapter(
    private val callback: AdapterCallback
) : BaseAdapter<Track>() {

    interface AdapterCallback {
        fun onPlayTrackClicked(track: Track, position: Int)
        fun onPauseTrackClicked(track: Track, position: Int)
        fun onPreviewTrackClicked(track: Track, position: Int)

        fun getTrack(position: Int): Track
    }

    override fun provideComparator() = compareBy(Track::trackId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Track> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView, callback)
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