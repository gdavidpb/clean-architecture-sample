package cl.yapo.test.ui.viewholders

import android.view.View
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.ui.adapters.ArtistAdapter
import cl.yapo.test.utils.onClickOnce
import kotlinx.android.synthetic.main.item_artist.view.*

open class ArtistViewHolder(
    itemView: View,
    private val callback: ArtistAdapter.AdapterCallback
) :
    BaseViewHolder<Artist>(itemView) {
    override fun bindView(item: Artist) {
        with(itemView) {
            cBoxLiked.setOnCheckedChangeListener(null)

            tViewArtistName.text = item.artistName
            tViewArtistGenre.text = item.primaryGenreName
            cBoxLiked.isChecked = item.isLiked

            cBoxLiked.setOnCheckedChangeListener { _, isChecked ->
                callback.onArtistLikeChanged(item, adapterPosition, isChecked)
            }

            onClickOnce {
                callback.onArtistClicked(item, adapterPosition)
            }
        }
    }
}