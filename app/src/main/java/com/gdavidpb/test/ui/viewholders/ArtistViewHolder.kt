package com.gdavidpb.test.ui.viewholders

import android.view.View
import android.widget.CompoundButton
import com.gdavidpb.test.presentation.model.ArtistItem
import com.gdavidpb.test.ui.adapters.ArtistAdapter
import com.gdavidpb.test.utils.extensions.onClickOnce
import kotlinx.android.synthetic.main.view_item_artist.view.*

class ArtistViewHolder(
        itemView: View,
        private val manager: ArtistAdapter.AdapterManager
) : BaseViewHolder<ArtistItem>(itemView) {

    private val likeChangedListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val item = getItem()

            if (item != null) manager.onArtistLikeChanged(item, isChecked)
        }

    init {
        with(itemView) {
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

            cBoxLiked.setOnCheckedChangeListener(null)

            cBoxLiked.isChecked = item.isLiked

            cBoxLiked.setOnCheckedChangeListener(likeChangedListener)
        }
    }
}