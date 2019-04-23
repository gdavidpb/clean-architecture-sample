package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.ui.adapters.AlbumAdapter
import com.gdavidpb.test.utils.CircleTransform
import com.gdavidpb.test.utils.drawables
import com.gdavidpb.test.utils.formatYear
import com.gdavidpb.test.utils.onClickOnce
import kotlinx.android.synthetic.main.item_album.view.*
import org.jetbrains.anko.imageResource

open class AlbumViewHolder(
    itemView: View,
    private val callback: AlbumAdapter.AdapterCallback
) : BaseViewHolder<Album>(itemView) {
    override fun bindView(item: Album) {
        with(itemView) {
            tViewAlbumName.text = item.collectionName
            tViewAlbumName.drawables(right = if (item.isExplicit) R.drawable.ic_explicit else 0)
            tViewAlbumArtistName.text = item.artistName
            tViewAlbumGenreAndYear.text = context.getString(
                R.string.text_album_genre_year,
                item.primaryGenreName,
                item.releaseDate.formatYear()
            )

            val artworkUrl = item.artworkUrl100

            if (artworkUrl.isNotBlank())
                with(receiver = callback.provideImageLoader()) {
                    load(artworkUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .transform(CircleTransform())
                        .into(iViewAlbumCover)
                }
            else
                iViewAlbumCover.imageResource = R.mipmap.ic_launcher_round

            onClickOnce {
                callback.onAlbumClicked(item, adapterPosition)
            }
        }
    }
}