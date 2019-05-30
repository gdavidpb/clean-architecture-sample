package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.ui.viewholders.AlbumViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.utils.SIZE_ALBUM_COVER
import com.gdavidpb.test.utils.onClickOnce
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_album.view.*

open class AlbumAdapter(
    private val manager: AdapterManager
) : BaseAdapter<Album>() {

    interface AdapterManager {
        fun onAlbumClicked(item: Album, position: Int)

        fun provideImageLoader(): Picasso
    }

    override fun provideComparator() = compareBy(Album::collectionId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Album> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)

        with(itemView) {
            iViewAlbumCover.updateLayoutParams<ViewGroup.LayoutParams> {
                width = SIZE_ALBUM_COVER
                height = SIZE_ALBUM_COVER
            }
        }

        return AlbumViewHolder(itemView, manager).also {
            with(itemView) {
                onClickOnce {
                    val item = it.resolveItem()

                    if (item != null)
                        manager.onAlbumClicked(item, it.adapterPosition)
                }
            }
        }
    }
}