package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.ui.viewholders.ArtistViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder

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

        return ArtistViewHolder(itemView, manager)
    }

    fun setArtistLiked(item: Artist, liked: Boolean) {
        updateItem(item) {
            copy(isLiked = liked)
        }
    }
}