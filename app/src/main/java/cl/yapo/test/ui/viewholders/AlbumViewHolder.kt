package cl.yapo.test.ui.viewholders

import android.view.View
import cl.yapo.test.R
import cl.yapo.test.domain.model.Album
import cl.yapo.test.ui.adapters.AlbumAdapter
import cl.yapo.test.utils.CircleTransform
import cl.yapo.test.utils.drawables
import cl.yapo.test.utils.formatYear
import cl.yapo.test.utils.onClickOnce
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