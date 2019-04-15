package cl.yapo.test.ui.activities

import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cl.yapo.test.R
import cl.yapo.test.domain.model.Track
import cl.yapo.test.domain.model.response.DownloadTrackResponse
import cl.yapo.test.domain.usecase.coroutines.Result
import cl.yapo.test.presentation.viewmodel.ArtistDetailViewModel
import cl.yapo.test.ui.adapters.TrackAdapter
import cl.yapo.test.utils.*
import kotlinx.android.synthetic.main.activity_album_detail.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AlbumDetailActivity : AppCompatActivity() {

    private val viewModel: ArtistDetailViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val trackAdapter = TrackAdapter(callback = TrackManager())

    private var mediaPlayer: MediaPlayer? = null

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

    override fun onPause() {
        super.onPause()

        trackAdapter.resetStates()

        resetPlayer()
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

                trackAdapter.updateItem(item = response.track) {
                    copy(
                        isPlaying = true,
                        isPaused = false,
                        isDownloading = false,
                        isDownloaded = true
                    )
                }

                startPlayer(file = response.file)
            }
            is Result.OnError -> {
                trackAdapter.resetStates()

                if (connectionManager.isNetworkAvailable())
                    longToast(R.string.toast_connection_failure)
                else
                    longToast(R.string.toast_no_connection)
            }
            else -> {
                trackAdapter.resetStates()

                longToast(R.string.toast_unexpected_failure)
            }
        }
    }

    private fun resetPlayer() {
        val previousPlayer = mediaPlayer

        if (previousPlayer != null) {
            if (previousPlayer.isPlaying) {
                previousPlayer.setOnCompletionListener(null)

                previousPlayer.stop()
                previousPlayer.release()
            }
        }
    }

    private fun startPlayer(file: File) {
        resetPlayer()

        mediaPlayer = MediaPlayer.create(applicationContext, Uri.fromFile(file))

        val newPlayer = mediaPlayer ?: return

        newPlayer.setOnCompletionListener {
            trackAdapter.resetStates()
        }

        newPlayer.start()
    }

    inner class TrackManager : TrackAdapter.AdapterCallback {
        override fun onPlayTrackClicked(track: Track, position: Int) {
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
            val player = mediaPlayer ?: return

            if (player.isPlaying) {
                trackAdapter.updateItem(position = position) {
                    copy(
                        isPlaying = false,
                        isPaused = true,
                        isDownloading = false
                    )
                }

                player.pause()
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
