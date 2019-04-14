package cl.yapo.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import cl.yapo.test.R
import cl.yapo.test.domain.model.Album
import cl.yapo.test.ui.viewholders.AlbumViewHolder
import cl.yapo.test.ui.viewholders.BaseViewHolder
import cl.yapo.test.utils.SIZE_ALBUM_COVER
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_album.view.*

open class AlbumAdapter(
    private val callback: AdapterCallback
) : BaseAdapter<Album>() {

    interface AdapterCallback {
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

        return AlbumViewHolder(itemView, callback)
    }
}