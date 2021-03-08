package com.gdavidpb.test.ui.viewholders

import android.view.View
import com.gdavidpb.test.presentation.model.ArtistItem
import com.gdavidpb.test.ui.adapters.ArtistAdapter
import com.gdavidpb.test.utils.extensions.onClickOnce
import com.gdavidpb.test.utils.extensions.setSafeChecked
import kotlinx.android.synthetic.main.item_artist.view.*

class ArtistViewHolder(
    itemView: View,
    private val manager: ArtistAdapter.AdapterManager
) : BaseViewHolder<ArtistItem>(itemView) {

    init {
        with(itemView) {
            cBoxLiked.setOnCheckedChangeListener { buttonView, isChecked ->
                /* Ignore event when it's disabled */
                if (!buttonView.isEnabled) return@setOnCheckedChangeListener

                val item = getItem()

                if (item != null) manager.onArtistLikeChanged(item, isChecked)
            }

            onClickOnce {
                val item = getItem()

                if (item != null) manager.onArtistClicked(item)
            }
        }
    }

    override fun bindView(item: ArtistItem) {
        super.bindView(item)

        with(itemView) {
            tViewArtistName.text = item.artistName
            tViewArtistGenre.text = item.primaryGenreName

            cBoxLiked.setSafeChecked(item.isLiked)
        }
    }
}