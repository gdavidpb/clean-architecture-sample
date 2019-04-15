package cl.yapo.test.ui.viewholders

import android.view.View
import cl.yapo.test.R
import cl.yapo.test.domain.model.Track
import cl.yapo.test.ui.adapters.TrackAdapter
import cl.yapo.test.utils.drawables
import cl.yapo.test.utils.formatInterval
import cl.yapo.test.utils.onClickOnce
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