package cl.yapo.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import cl.yapo.test.R
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.ui.viewholders.ArtistViewHolder
import cl.yapo.test.ui.viewholders.BaseViewHolder

open class ArtistAdapter(
    private val callback: AdapterCallback
) : BaseAdapter<Artist>() {

    interface AdapterCallback {
        fun onArtistClicked(item: Artist, position: Int)
        fun onArtistLikeChanged(item: Artist, position: Int, liked: Boolean)
    }

    override fun provideComparator() = compareBy(Artist::artistId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Artist> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)

        return ArtistViewHolder(itemView, callback)
    }

    fun setArtistLiked(liked: Boolean, position: Int) {
        items[position] = items[position].copy(isLiked = liked)

        notifyItemChanged(position)
    }

    fun removeLikedArtist(position: Int) {
        items.removeAt(position)

        notifyItemRemoved(position)
    }

    fun isEmpty() = items.isEmpty()
}