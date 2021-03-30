package com.gdavidpb.test.ui.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.onClickOnce
import com.gdavidpb.test.utils.extensions.visible
import kotlinx.android.synthetic.main.view_item_track.view.*

class TrackViewHolder(
    itemView: View,
    private val manager: TrackAdapter.AdapterManager
) : BaseViewHolder<TrackItem>(itemView) {

    init {
        with(itemView) {
            onClickOnce {
                val item = getItem()

                when {
                    item.isMusic && !item.isPlaying -> manager.onPlayTrackClicked(item)
                    item.isMusic && item.isPlaying -> manager.onPauseTrackClicked(item)
                    item.isVideo -> manager.onPreviewTrackClicked(item)
                }
            }
        }
    }

    override fun bindView(item: TrackItem) {
        super.bindView(item)

        with(itemView) {
            tViewTrackName.drawables(right = item.nameIconResource)

            val textColorResource = item.textColorResource()
            val actionIconResource = item.actionIconResource()

            val textColor = ContextCompat.getColor(context, textColorResource)

            tViewTrackName.setTextColor(textColor)
            tViewTrackTime.setTextColor(textColor)

            tViewTrackName.text = item.trackName
            tViewTrackTime.text = item.trackTimeMillis

            pBarTrack.visible = item.isLoading
            iViewTrackAction.visible = !item.isLoading

            if (actionIconResource != 0)
                iViewTrackAction.setImageResource(actionIconResource)
            else
                iViewTrackAction.setImageDrawable(null)
        }
    }

    private fun TrackItem.actionIconResource() = when {
        isPlaying -> R.drawable.ic_pause
        isPaused -> R.drawable.ic_play_on
        isLoading -> R.drawable.ic_play
        isMusic -> R.drawable.ic_play
        isVideo -> R.drawable.ic_video
        else -> throw NoWhenBranchMatchedException()
    }

    private fun TrackItem.textColorResource() = when {
        isPlaying || isPaused || isLoading -> R.color.colorAccent
        else -> R.color.material_on_surface_emphasis_high_type
    }
}