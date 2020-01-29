package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.utils.extensions.setSafeChecked
import kotlinx.android.synthetic.main.item_artist.view.*

open class ArtistViewHolder(
    itemView: View
) :
    BaseViewHolder<Artist>(itemView) {
    override fun bindView(item: Artist) {
        with(itemView) {
            tViewArtistName.text = item.artistName
            tViewArtistGenre.text = item.primaryGenreName

            cBoxLiked.setSafeChecked(item.isLiked)
        }
    }
}