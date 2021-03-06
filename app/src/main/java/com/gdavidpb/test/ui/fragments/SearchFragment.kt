package com.gdavidpb.test.ui.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.presentation.state.SearchState
import com.gdavidpb.test.presentation.viewmodel.SearchViewModel
import com.gdavidpb.test.ui.adapters.ArtistAdapter
import com.gdavidpb.test.utils.IdempotentLocker
import com.gdavidpb.test.utils.TIME_SEARCHER_LOCKER
import com.gdavidpb.test.utils.extensions.drawables
import com.gdavidpb.test.utils.extensions.isNetworkAvailable
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.extensions.toast
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

open class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val artistAdapter = ArtistAdapter(manager = ArtistManager())

    private val locker = IdempotentLocker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rViewSearch) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = artistAdapter

            setHasFixedSize(true)
        }

        eTextSearch.doOnTextChanged { text, _, _, _ ->
            if (eTextSearch.isFocused) {
                locker.executeLast(TIME_SEARCHER_LOCKER) {
                    viewModel.setState {
                        copy(query = "$text")
                    }
                }
            }
        }

        with(viewModel) {
            observe(state, ::stateObserver)
            observe(artists, ::artistsObserver)
        }
    }

    private fun stateObserver(state: SearchState?) {
        state ?: return

        pBarSearch.visibility = View.VISIBLE

        val query = state.query

        if (query.isNotBlank())
            viewModel.searchArtists(term = query)
        else
            viewModel.resetSearch()
    }

    private fun artistsObserver(result: Result<List<Artist>>?) {
        when (result) {
            is Result.OnLoading -> {
                pBarSearch.visibility = View.VISIBLE
            }
            is Result.OnSuccess -> {
                pBarSearch.visibility = View.GONE

                val artists = result.value
                    .sortedBy { it.artistName }

                val isSearch = eTextSearch.text.toString().isNotBlank()

                if (artists.isNotEmpty()) {
                    tViewSearch.visibility = View.GONE
                    rViewSearch.visibility = View.VISIBLE
                } else {
                    if (isSearch) {
                        tViewSearch.setText(R.string.text_search_no_results)
                        tViewSearch.drawables(top = R.drawable.ic_search_no_results)
                    } else {
                        tViewSearch.setText(R.string.text_search_empty)
                        tViewSearch.drawables(top = R.drawable.ic_search_empty)
                    }

                    rViewSearch.visibility = View.GONE
                    tViewSearch.visibility = View.VISIBLE
                }

                artistAdapter.swapItems(new = artists)
            }
            is Result.OnError -> {
                pBarSearch.visibility = View.GONE

                val messageResource = if (connectionManager.isNetworkAvailable())
                    R.string.toast_connection_failure
                else
                    R.string.toast_no_connection

                requireContext().toast(messageResource)
            }
            else -> {
                pBarSearch.visibility = View.GONE

                requireContext().toast(R.string.toast_unexpected_failure)
            }
        }
    }

    inner class ArtistManager : ArtistAdapter.AdapterManager {
        override fun onArtistClicked(item: Artist, position: Int) {
            // TODO startActivity<ArtistActivity>(EXTRA_ARTIST_ID to item.artistId)
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
