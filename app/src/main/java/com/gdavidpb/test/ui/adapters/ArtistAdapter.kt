package com.gdavidpb.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.ui.viewholders.ArtistViewHolder
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import com.gdavidpb.test.utils.extensions.onClickOnce
import kotlinx.android.synthetic.main.item_artist.view.*

class ArtistAdapter(
    private val manager: AdapterManager
) : BaseAdapter<Artist>() {

    interface AdapterManager {
        fun onArtistClicked(item: Artist, position: Int)
        fun onArtistLikeChanged(item: Artist, position: Int, liked: Boolean)
    }

    override fun provideComparator() = compareBy(Artist::artistId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Artist> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)

        return ArtistViewHolder(itemView).also {
            with(itemView) {
                cBoxLiked.setOnCheckedChangeListener { buttonView, isChecked ->
                    /* Ignore event when it's disabled */
                    if (!buttonView.isEnabled) return@setOnCheckedChangeListener

                    val item = it.resolveItem()

                    if (item != null)
                        manager.onArtistLikeChanged(item, it.bindingAdapterPosition, isChecked)
                }

                onClickOnce {
                    val item = it.resolveItem()

                    if (item != null)
                        manager.onArtistClicked(item, it.bindingAdapterPosition)
                }
            }
        }
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