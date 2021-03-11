package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.AlbumItem
import com.gdavidpb.test.ui.viewholders.AlbumViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.utils.Sizes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_item_album.view.*

class AlbumAdapter(
    private val manager: AdapterManager
) : BaseAdapter<AlbumItem>(AdapterComparator.comparator) {

    init {
        setHasStableIds(true)
    }

    object AdapterComparator {
        val comparator = compareBy(AlbumItem::collectionId)
    }

    interface AdapterManager {
        fun onAlbumClicked(item: AlbumItem)
        fun provideImageLoader(): Picasso
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).collectionId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AlbumItem> {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_item_album, parent, false)

        with(itemView) {
            iViewAlbumCover.updateLayoutParams<ViewGroup.LayoutParams> {
                width = Sizes.ALBUM_COVER
                height = Sizes.ALBUM_COVER
            }
        }

        return AlbumViewHolder(itemView, manager)
    }
}