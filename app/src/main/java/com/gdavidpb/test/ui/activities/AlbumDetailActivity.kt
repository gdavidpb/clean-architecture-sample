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
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.presentation.viewmodel.ArtistDetailViewModel
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.EXTRA_ALBUM_ID
import com.gdavidpb.test.utils.MediaPlayerManager
import com.gdavidpb.test.utils.extensions.isNetworkAvailable
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.extensions.toast
import com.gdavidpb.test.utils.mappers.toTrack
import com.gdavidpb.test.utils.mappers.toTrackItem
import kotlinx.android.synthetic.main.activity_album_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumDetailActivity : AppCompatActivity() {

    private val viewModel: ArtistDetailViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val trackAdapter = TrackAdapter(manager = TrackManager())

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

            sRefreshAlbumDetail.setOnRefreshListener {
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

                val items = tracks.map(Track::toTrackItem)

                trackAdapter.swapItems(new = items)
            }
            is Result.OnError -> {
                sRefreshAlbumDetail.isRefreshing = false

                val messageResource = if (connectionManager.isNetworkAvailable())
                    R.string.toast_connection_failure
                else
                    R.string.toast_no_connection

                toast(messageResource)
            }
            else -> {
                sRefreshAlbumDetail.isRefreshing = false

                toast(R.string.toast_unexpected_failure)
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
                    val track = response.track.let(Track::toTrackItem)

                    trackAdapter.updateItem(track) {
                        copy(
                            isPlaying = true,
                            isPaused = false,
                            isDownloading = false,
                            isDownloaded = true
                        )
                    }

                    mediaPlayerManager.play(source = response.file) {
                        trackAdapter.updateItem(track) {
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
                val messageResource = if (connectionManager.isNetworkAvailable())
                    R.string.toast_connection_failure
                else
                    R.string.toast_no_connection

                toast(messageResource)
            }
        }
    }

    inner class TrackManager : TrackAdapter.AdapterManager {
        override fun onPlayTrackClicked(track: TrackItem, position: Int) {
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

            viewModel.downloadTrack(track = track.let(TrackItem::toTrack))
        }

        override fun onPauseTrackClicked(track: TrackItem, position: Int) {
            mediaPlayerManager.pause()

            trackAdapter.updateItem(position = position) {
                copy(
                    isPlaying = false,
                    isPaused = true,
                    isDownloading = false
                )
            }
        }

        override fun onPreviewTrackClicked(track: TrackItem, position: Int) {
            /* todo
            startActivity<WebViewActivity>(
                EXTRA_TITLE to track.trackName,
                EXTRA_URL to track.previewUrl
            )
            */
        }
    }
}
