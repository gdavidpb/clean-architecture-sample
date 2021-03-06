package com.gdavidpb.test.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.domain.usecase.errors.LookupAlbumsError
import com.gdavidpb.test.presentation.model.AlbumItem
import com.gdavidpb.test.presentation.viewmodel.ArtistViewModel
import com.gdavidpb.test.ui.adapters.AlbumAdapter
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.mappers.toAlbumItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_artist.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistFragment : NavigationFragment() {

    private val viewModel: ArtistViewModel by viewModel()

    private val picasso: Picasso by inject()

    private val albumAdapter = AlbumAdapter(manager = AlbumManager())

    private val args by navArgs<ArtistFragmentArgs>()

    override fun onCreateView() = R.layout.fragment_artist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rViewAlbums) {
            layoutManager = LinearLayoutManager(context)
            adapter = albumAdapter
            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(albums, ::onGetAlbums)

            lookupAlbums(artistId = args.artistId)

            sRefreshAlbums.setOnRefreshListener {
                lookupAlbums(artistId = args.artistId)
            }
        }
    }

    private fun onGetAlbums(result: Result<List<Album>, LookupAlbumsError>?) {
        when (result) {
            is Result.OnLoading -> {
                sRefreshAlbums.isRefreshing = true
            }
            is Result.OnSuccess -> {
                handleOnGetAlbumsSuccess(albums = result.value)
            }
            is Result.OnError -> {
                handleOnGetAlbumsError(error = result.error)
            }
            else -> {
                sRefreshAlbums.isRefreshing = false

                snackBar {
                    messageResource = R.string.snack_bar_unexpected_failure
                }
            }
        }
    }

    private fun handleOnGetAlbumsSuccess(albums: List<Album>) {
        sRefreshAlbums.isRefreshing = false

        val context = requireContext()

        val items = albums.map { it.toAlbumItem(context) }

        albumAdapter.submitList(items)

        if (items.isNotEmpty()) {
            tViewAlbums.visibility = View.GONE
            rViewAlbums.visibility = View.VISIBLE
        } else {
            rViewAlbums.visibility = View.GONE
            tViewAlbums.visibility = View.VISIBLE
        }
    }

    private fun handleOnGetAlbumsError(error: LookupAlbumsError?) {
        sRefreshAlbums.isRefreshing = false

        when (error) {
            is LookupAlbumsError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = error.isNetworkAvailable)
            else -> defaultErrorSnackBar()
        }
    }

    inner class AlbumManager : AlbumAdapter.AdapterManager {
        override fun onAlbumClicked(item: AlbumItem) {
            val destination = ArtistFragmentDirections.navToAlbum(
                albumId = item.collectionId,
                albumName = item.collectionName.toString()
            )

            navigate(destination)
        }

        override fun provideImageLoader(): Picasso {
            return picasso
        }
    }
}
