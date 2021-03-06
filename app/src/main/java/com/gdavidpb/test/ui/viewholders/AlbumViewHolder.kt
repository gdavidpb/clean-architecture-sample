package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.ui.adapters.AlbumAdapter
import com.gdavidpb.test.utils.CircleTransform
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.formatYear
import kotlinx.android.synthetic.main.item_album.view.*

open class AlbumViewHolder(
    itemView: View,
    private val manager: AlbumAdapter.AdapterManager
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