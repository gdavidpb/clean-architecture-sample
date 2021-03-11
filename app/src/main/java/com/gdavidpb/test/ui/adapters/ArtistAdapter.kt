package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.ArtistItem
import com.gdavidpb.test.ui.viewholders.ArtistViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder

class ArtistAdapter(
    private val manager: AdapterManager
) : BaseAdapter<ArtistItem>(AdapterComparator.comparator) {

    init {
        setHasStableIds(true)
    }

    object AdapterComparator {
        val comparator = compareBy(ArtistItem::artistId)
    }

    interface AdapterManager {
        fun onArtistClicked(item: ArtistItem)
        fun onArtistLikeChanged(item: ArtistItem, liked: Boolean)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).artistId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ArtistItem> {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_item_artist, parent, false)

        return ArtistViewHolder(itemView, manager)
    }

    fun setArtistLiked(item: ArtistItem, liked: Boolean) {
        updateItem(item) {
            copy(isLiked = liked)
        }
    }
}