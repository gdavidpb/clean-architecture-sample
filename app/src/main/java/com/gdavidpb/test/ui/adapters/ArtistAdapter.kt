package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.ui.viewholders.ArtistViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.utils.extensions.onClickOnce
import kotlinx.android.synthetic.main.item_artist.view.*

class ArtistAdapter(
    private val manager: AdapterManager
) : BaseAdapter<Artist>(AdapterComparator.comparator) {

    object AdapterComparator {
        val comparator = compareBy(Artist::artistId)
    }

    interface AdapterManager {
        fun onArtistClicked(item: Artist)
        fun onArtistLikeChanged(item: Artist, liked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Artist> {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_artist, parent, false)

        return ArtistViewHolder(itemView).also {
            with(itemView) {
                cBoxLiked.setOnCheckedChangeListener { buttonView, isChecked ->
                    /* Ignore event when it's disabled */
                    if (!buttonView.isEnabled) return@setOnCheckedChangeListener

                    val item = it.getItem()

                    if (item != null)
                        manager.onArtistLikeChanged(item, isChecked)
                }

                onClickOnce {
                    val item = it.getItem()

                    if (item != null)
                        manager.onArtistClicked(item)
                }
            }
        }
    }

    fun setArtistLiked(item: Artist, liked: Boolean) {
        updateItem(item) {
            copy(isLiked = liked)
        }
    }
}