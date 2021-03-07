package com.gdavidpb.test.ui.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.domain.usecase.errors.SearchArtistsError
import com.gdavidpb.test.presentation.state.SearchState
import com.gdavidpb.test.presentation.viewmodel.SearchViewModel
import com.gdavidpb.test.ui.adapters.ArtistAdapter
import com.gdavidpb.test.utils.IdempotentLocker
import com.gdavidpb.test.utils.TIME_SEARCHER_LOCKER
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.isNetworkAvailable
import com.gdavidpb.test.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : NavigationFragment() {

    private val viewModel: SearchViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val artistAdapter = ArtistAdapter(manager = ArtistManager())

    private val locker = IdempotentLocker()

    override fun onCreateView() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rViewSearch) {
            layoutManager = LinearLayoutManager(context)
            adapter = artistAdapter

            setHasFixedSize(true)
        }

        eTextSearch.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    viewModel.resetSearch()
                }
                eTextSearch.isFocused -> {
                    locker.executeLast(TIME_SEARCHER_LOCKER) {
                        viewModel.setState {
                            copy(query = "$text")
                        }
                    }
                }
            }
        }

        with(viewModel) {
            observe(state, ::onStateChanged)
            observe(artists, ::onGetArtists)
        }
    }

    private fun onStateChanged(state: SearchState?) {
        state ?: return

        pBarSearch.visibility = View.VISIBLE

        val query = state.query

        if (query.isNotBlank())
            viewModel.searchArtists(term = query)
        else
            viewModel.resetSearch()
    }

    private fun onGetArtists(result: Result<List<Artist>, SearchArtistsError>?) {
        when (result) {
            is Result.OnLoading -> {
                pBarSearch.visibility = View.VISIBLE
            }
            is Result.OnSuccess -> {
                handleOnGetArtistsSuccess(artists = result.value)
            }
            is Result.OnError -> {
                handleOnGetArtistsError(error = result.error)
            }
            else -> {
                pBarSearch.visibility = View.GONE

                snackBar {
                    messageResource = R.string.snack_bar_unexpected_failure
                }
            }
        }
    }

    private fun handleOnGetArtistsSuccess(artists: List<Artist>) {
        pBarSearch.visibility = View.GONE

        artistAdapter.swapItems(new = artists)

        val isSearch = eTextSearch.text.toString().isNotBlank()

        when {
            artists.isNotEmpty() -> {
                tViewSearch.visibility = View.GONE
                rViewSearch.visibility = View.VISIBLE
            }
            isSearch -> {
                tViewSearch.setText(R.string.text_search_no_results)
                tViewSearch.drawables(top = R.drawable.ic_search_no_results)

                rViewSearch.visibility = View.GONE
                tViewSearch.visibility = View.VISIBLE
            }
            !isSearch -> {
                tViewSearch.setText(R.string.text_search_empty)
                tViewSearch.drawables(top = R.drawable.ic_search_empty)

                rViewSearch.visibility = View.GONE
                tViewSearch.visibility = View.VISIBLE
            }
        }
    }

    private fun handleOnGetArtistsError(error: SearchArtistsError?) {
        pBarSearch.visibility = View.GONE

        when (error) {
            is SearchArtistsError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = connectionManager.isNetworkAvailable())
            else -> defaultErrorSnackBar()
        }
    }

    inner class ArtistManager : ArtistAdapter.AdapterManager {
        override fun onArtistClicked(item: Artist, position: Int) {
            val destination = SearchFragmentDirections.navToArtist(
                artistId = item.artistId,
                artistName = item.artistName
            )

            navigate(destination)
        }

        override fun onArtistLikeChanged(item: Artist, position: Int, liked: Boolean) {
            artistAdapter.setArtistLiked(liked, position)

            if (liked)
                viewModel.likeArtist(artistId = item.artistId)
            else
                viewModel.unlikeArtist(artistId = item.artistId)
        }
    }
}
