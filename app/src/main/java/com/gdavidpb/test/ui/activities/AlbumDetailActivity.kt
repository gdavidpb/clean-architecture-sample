package com.gdavidpb.test.ui.activities

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.presentation.viewmodel.ArtistDetailViewModel
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.*
import kotlinx.android.synthetic.main.activity_album_detail.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumDetailActivity : AppCompatActivity() {

    private val viewModel: ArtistDetailViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val trackAdapter = TrackAdapter(callback = TrackManager())

    private lateinit var mediaPlayerManager: MediaPlayerManager

    private val extraAlbumId by lazy {
        intent.getLongExtra(EXTRA_ALBUM_ID, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sRefreshAlbumDetail.isEnabled = false

        with(rViewAlbumDetail) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = trackAdapter
            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(tracks, ::tracksObserver)
            observe(download, ::downloadObserver)

            lookupTracks(albumId = extraAlbumId)

            sRefreshAlbumDetail.onRefresh {
                lookupTracks(albumId = extraAlbumId)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onResume() {
        super.onResume()

        mediaPlayerManager = MediaPlayerManager(this)
    }

    override fun onPause() {
        mediaPlayerManager.release()

        trackAdapter.resetStates()

        super.onPause()
    }

    override fun onStop() {
        mediaPlayerManager.release()

        super.onStop()
    }

    private fun tracksObserver(result: Result<List<Track>>?) {
        when (result) {
            is Result.OnLoading -> {
                sRefreshAlbumDetail.isRefreshing = true
            }
            is Result.OnSuccess -> {
                sRefreshAlbumDetail.isRefreshing = false

                val tracks = result.value

                if (tracks.isNotEmpty())
                    supportActionBar?.title = tracks.first().collectionName

                trackAdapter.swapItems(new = tracks)
            }
            is Result.OnError -> {
                sRefreshAlbumDetail.isRefreshing = false

                if (connectionManager.isNetworkAvailable())
                    longToast(R.string.toast_connection_failure)
                else
                    longToast(R.string.toast_no_connection)
            }
            else -> {
                sRefreshAlbumDetail.isRefreshing = false

                longToast(R.string.toast_unexpected_failure)
            }
        }
    }

    private fun downloadObserver(result: Result<DownloadTrackResponse>?) {
        when (result) {
            is Result.OnLoading -> {

            }
            is Result.OnSuccess -> {
                val response = result.value

                val isReady = !response.track.isDownloading || response.track.isDownloaded

                if (isReady) {
                    trackAdapter.updateItem(item = response.track) {
                        copy(
                            isPlaying = true,
                            isPaused = false,
                            isDownloading = false,
                            isDownloaded = true
                        )
                    }

                    mediaPlayerManager.play(source = response.file) {
                        trackAdapter.updateItem(item = response.track) {
                            copy(
                                isPlaying = false,
                                isPaused = false,
                                isDownloading = false
                            )
                        }
                    }
                }
            }
            is Result.OnError -> {
                if (connectionManager.isNetworkAvailable())
                    longToast(R.string.toast_connection_failure)
                else
                    longToast(R.string.toast_no_connection)
            }
        }
    }

    inner class TrackManager : TrackAdapter.AdapterCallback {
        override fun onPlayTrackClicked(track: Track, position: Int) {
            mediaPlayerManager.stop()

            if (track.isDownloaded)
                trackAdapter.updateItem(position = position) {
                    copy(
                        isPlaying = true,
                        isPaused = false,
                        isDownloading = false
                    )
                }
            else
                trackAdapter.updateItem(position = position) {
                    copy(
                        isPlaying = false,
                        isPaused = false,
                        isDownloading = true
                    )
                }

            viewModel.downloadTrack(track)
        }

        override fun onPauseTrackClicked(track: Track, position: Int) {
            mediaPlayerManager.pause()

            trackAdapter.updateItem(position = position) {
                copy(
                    isPlaying = false,
                    isPaused = true,
                    isDownloading = false
                )
            }
        }

        override fun onPreviewTrackClicked(track: Track, position: Int) {
            startActivity<WebViewActivity>(
                EXTRA_TITLE to track.trackName,
                EXTRA_URL to track.previewUrl
            )
        }

        override fun getTrack(position: Int): Track {
            return trackAdapter.getItem(position)
        }
    }
}
