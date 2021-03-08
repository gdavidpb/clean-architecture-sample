package com.gdavidpb.test.ui.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.onClickOnce
import com.gdavidpb.test.utils.extensions.visible
import kotlinx.android.synthetic.main.item_track.view.*

class TrackViewHolder(
    itemView: View,
    private val manager: TrackAdapter.AdapterManager
) : BaseViewHolder<TrackItem>(itemView) {

    init {
        with(itemView) {
            onClickOnce {
                val item = getItem()

                if (item != null)
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

            val textColorResource = item.computeTextColorResource()
            val actionIconResource = item.computeActionIconResource()

            val textColor = ContextCompat.getColor(context, textColorResource)

            tViewTrackName.setTextColor(textColor)
            tViewTrackTime.setTextColor(textColor)

            tViewTrackName.text = item.trackName
            tViewTrackTime.text = item.timeMillisString

            pBarTrack.visible = item.isDownloading
            iViewTrackAction.visible = !item.isDownloading

            if (actionIconResource != 0)
                iViewTrackAction.setImageResource(actionIconResource)
            else
                iViewTrackAction.setImageDrawable(null)
        }
    }
}