package com.gdavidpb.test.ui.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.presentation.viewmodel.FavoritesViewModel
import com.gdavidpb.test.ui.adapters.ArtistAdapter
import com.gdavidpb.test.utils.extensions.isNetworkAvailable
import com.gdavidpb.test.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : NavigationFragment() {

    private val viewModel: FavoritesViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val artistAdapter = ArtistAdapter(manager = ArtistManager())

    override fun onCreateView() = R.layout.fragment_favorites

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sRefreshFavorites.isEnabled = false

        with(rViewFavorites) {
            layoutManager = LinearLayoutManager(context)
            adapter = artistAdapter

            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(liked, ::onGetLikedArtists)

            getLikedArtists()
        }
    }

    private fun onGetLikedArtists(result: Result<List<Artist>>?) {
        when (result) {
            is Result.OnLoading -> {
                sRefreshFavorites.isRefreshing = true
            }
            is Result.OnSuccess -> {
                handleOnGetLikedArtistsSuccess(artists = result.value)
            }
            is Result.OnError -> {
                handleOnGetLikedArtistsError()
            }
            else -> {
                sRefreshFavorites.isRefreshing = false

                snackBar {
                    messageResource = R.string.snack_bar_unexpected_failure
                }
            }
        }
    }

    private fun handleOnGetLikedArtistsSuccess(artists: List<Artist>) {
        sRefreshFavorites.isRefreshing = false

        if (artists.isNotEmpty()) {
            tViewFavorites.visibility = View.GONE
            rViewFavorites.visibility = View.VISIBLE
        } else {
            rViewFavorites.visibility = View.GONE
            tViewFavorites.visibility = View.VISIBLE
        }

        artistAdapter.swapItems(new = artists)
    }

    private fun handleOnGetLikedArtistsError() {
        sRefreshFavorites.isRefreshing = false

        noConnectionSnackBar(isNetworkAvailable = connectionManager.isNetworkAvailable())
    }

    inner class ArtistManager : ArtistAdapter.AdapterManager {
        override fun onArtistClicked(item: Artist, position: Int) {
            val destination = FavoritesFragmentDirections.navToArtist(
                artistId = item.artistId,
                artistName = item.artistName
            )

            navigate(destination)
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
