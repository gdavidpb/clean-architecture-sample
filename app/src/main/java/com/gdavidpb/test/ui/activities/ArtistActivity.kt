package com.gdavidpb.test.ui.activities

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.presentation.viewmodel.ArtistViewModel
import com.gdavidpb.test.ui.adapters.AlbumAdapter
import com.gdavidpb.test.utils.EXTRA_ALBUM_ID
import com.gdavidpb.test.utils.EXTRA_ARTIST_ID
import com.gdavidpb.test.utils.isNetworkAvailable
import com.gdavidpb.test.utils.observe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artist.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistActivity : AppCompatActivity() {

    private val viewModel: ArtistViewModel by viewModel()

    private val picasso: Picasso by inject()

    private val connectionManager: ConnectivityManager by inject()

    private val albumAdapter = AlbumAdapter(manager = AlbumManager())

    private val extraArtistId by lazy {
        intent.getLongExtra(EXTRA_ARTIST_ID, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(rViewAlbums) {
            layoutManager = LinearLayoutManager(context)
            adapter = albumAdapter
            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(albums, ::albumsObserver)

            lookupAlbums(artistId = extraArtistId)

            sRefreshAlbums.onRefresh {
                lookupAlbums(artistId = extraArtistId)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun albumsObserver(result: Result<List<Album>>?) {
        when (result) {
            is Result.OnLoading -> {
                sRefreshAlbums.isRefreshing = true
            }
            is Result.OnSuccess -> {
                sRefreshAlbums.isRefreshing = false

                val albums = result.value
                    .sortedByDescending { it.releaseDate }

                if (albums.isNotEmpty()) {
                    supportActionBar?.title = albums.first().artistName

                    tViewAlbums.visibility = View.GONE
                    rViewAlbums.visibility = View.VISIBLE
                } else {
                    rViewAlbums.visibility = View.GONE
                    tViewAlbums.visibility = View.VISIBLE
                }

                albumAdapter.swapItems(new = albums)
            }
            is Result.OnError -> {
                sRefreshAlbums.isRefreshing = false

                if (connectionManager.isNetworkAvailable())
                    longToast(R.string.toast_connection_failure)
                else
                    longToast(R.string.toast_no_connection)
            }
            else -> {
                sRefreshAlbums.isRefreshing = false

                longToast(R.string.toast_unexpected_failure)
            }
        }
    }

    inner class AlbumManager : AlbumAdapter.AdapterManager {
        override fun onAlbumClicked(item: Album, position: Int) {
            startActivity<AlbumDetailActivity>(EXTRA_ALBUM_ID to item.collectionId)
        }

        override fun provideImageLoader(): Picasso {
            return picasso
        }
    }
}
