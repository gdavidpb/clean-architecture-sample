package cl.yapo.test.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cl.yapo.test.R
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.usecase.coroutines.Result
import cl.yapo.test.presentation.viewmodel.FavoritesViewModel
import cl.yapo.test.ui.adapters.ArtistAdapter
import cl.yapo.test.utils.observe
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.androidx.viewmodel.ext.android.viewModel

open class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()

    private val artistAdapter = ArtistAdapter(callback = ArtistManager())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rViewFavorites) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = artistAdapter

            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(liked, ::likedObserver)

            getLikedArtists()
        }
    }

    private fun likedObserver(result: Result<List<Artist>>?) {
        when (result) {
            is Result.OnLoading -> {

            }
            is Result.OnSuccess -> {
                val artists = result.value
                    .sortedBy { it.artistName }

                if (artists.isNotEmpty()) {
                    tViewFavorites.visibility = View.GONE
                    rViewFavorites.visibility = View.VISIBLE
                } else {
                    rViewFavorites.visibility = View.GONE
                    tViewFavorites.visibility = View.VISIBLE
                }

                artistAdapter.swapItems(new = artists)
            }
            is Result.OnError -> {

            }
            else -> {

            }
        }
    }

    inner class ArtistManager : ArtistAdapter.AdapterCallback {
        override fun onArtistClicked(item: Artist, position: Int) {

        }

        override fun onArtistLikeChanged(item: Artist, position: Int, liked: Boolean) {
            if (!liked) {
                artistAdapter.removeLikedArtist(position)

                viewModel.unlikeArtist(artistId = item.artistId)

                if (artistAdapter.isEmpty()) {
                    rViewFavorites.visibility = View.GONE
                    tViewFavorites.visibility = View.VISIBLE
                } else {
                    tViewFavorites.visibility = View.GONE
                    rViewFavorites.visibility = View.VISIBLE
                }
            }
        }
    }
}
