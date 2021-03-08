package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.R
import com.gdavidpb.test.presentation.model.AlbumItem
import com.gdavidpb.test.ui.adapters.AlbumAdapter
import com.gdavidpb.test.utils.CircleTransform
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.onClickOnce
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumViewHolder(
    itemView: View,
    private val manager: AlbumAdapter.AdapterManager
) : BaseViewHolder<AlbumItem>(itemView) {

    init {
        with(itemView) {
            onClickOnce {
                val item = getItem()

                if (item != null) manager.onAlbumClicked(item)
            }
        }
    }

    override fun bindView(item: AlbumItem) {
        super.bindView(item)

        with(itemView) {
            tViewAlbumName.text = item.collectionName
            tViewAlbumName.drawables(right = item.nameIconResource)
            tViewAlbumArtistName.text = item.artistName
            tViewAlbumGenreAndYear.text = item.genreAndYear

            val artworkUrl = item.artworkUrl

            if (artworkUrl.isNotBlank())
                with(receiver = manager.provideImageLoader()) {
                    load(artworkUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .transform(CircleTransform())
                        .into(iViewAlbumCover)
                }
            else
                iViewAlbumCover.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}