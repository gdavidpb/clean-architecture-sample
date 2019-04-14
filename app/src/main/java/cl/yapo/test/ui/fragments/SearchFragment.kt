package cl.yapo.test.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cl.yapo.test.R
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.usecase.coroutines.Result
import cl.yapo.test.presentation.state.SearchState
import cl.yapo.test.presentation.viewmodel.SearchViewModel
import cl.yapo.test.ui.activities.ArtistActivity
import cl.yapo.test.ui.adapters.ArtistAdapter
import cl.yapo.test.utils.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.textResource
import org.koin.androidx.viewmodel.ext.android.viewModel

open class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    private val artistAdapter = ArtistAdapter(callback = ArtistManager())

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
                locker.executeLast(250) {
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
                        tViewSearch.textResource = R.string.text_search_no_results
                        tViewSearch.drawables(top = R.drawable.ic_search_no_results)
                    } else {
                        tViewSearch.textResource = R.string.text_search_empty
                        tViewSearch.drawables(top = R.drawable.ic_search_empty)
                    }

                    rViewSearch.visibility = View.GONE
                    tViewSearch.visibility = View.VISIBLE
                }

                artistAdapter.swapItems(new = artists)
            }
            is Result.OnError -> {
                pBarSearch.visibility = View.GONE
            }
            else -> {
                pBarSearch.visibility = View.GONE
            }
        }
    }

    inner class ArtistManager : ArtistAdapter.AdapterCallback {
        override fun onArtistClicked(item: Artist, position: Int) {
            startActivity<ArtistActivity>(
                EXTRA_ARTIST_ID to item.artistId,
                EXTRA_ARTIST_NAME to item.artistName
            )
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
