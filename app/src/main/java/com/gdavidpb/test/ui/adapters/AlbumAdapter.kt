package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.AlbumItem
import com.gdavidpb.test.ui.viewholders.AlbumViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.utils.SIZE_ALBUM_COVER
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumAdapter(
    private val manager: AdapterManager
) : BaseAdapter<AlbumItem>(AdapterComparator.comparator) {

    object AdapterComparator {
        val comparator = compareBy(AlbumItem::collectionId)
    }

    interface AdapterManager {
        fun onAlbumClicked(item: AlbumItem)
        fun provideImageLoader(): Picasso
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AlbumItem> {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_album, parent, false)

        with(itemView) {
            iViewAlbumCover.updateLayoutParams<ViewGroup.LayoutParams> {
                width = SIZE_ALBUM_COVER
                height = SIZE_ALBUM_COVER
            }
        }

        return AlbumViewHolder(itemView, manager)
    }
}