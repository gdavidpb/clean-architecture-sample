package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.drawables
import com.gdavidpb.test.utils.visible
import kotlinx.android.synthetic.main.item_track.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource

open class TrackViewHolder(
    itemView: View
) : BaseViewHolder<TrackItem>(itemView) {
    override fun bindView(item: TrackItem) {
        with(itemView) {
            tViewTrackName.drawables(right = item.nameIconResource)

            val textColorResource = item.computeTextColorResource()
            val actionIconResource = item.computeActionIconResource()

            tViewTrackName.textColorResource = textColorResource
            tViewTrackTime.textColorResource = textColorResource
            iViewTrackAction.imageResource = actionIconResource

            tViewTrackName.text = item.trackName
            tViewTrackTime.text = item.timeMillisString

            pBarTrack.visible = item.isDownloading
            iViewTrackAction.visible = !item.isDownloading

            if (actionIconResource != 0) iViewTrackAction.imageResource = actionIconResource
        }
    }
}