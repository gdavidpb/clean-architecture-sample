package com.gdavidpb.test.ui.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.presentation.viewmodel.AlbumViewModel
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.MediaPlayerManager
import com.gdavidpb.test.utils.extensions.isNetworkAvailable
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.mappers.toTrack
import com.gdavidpb.test.utils.mappers.toTrackItem
import kotlinx.android.synthetic.main.fragment_album.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : NavigationFragment() {

    private val viewModel: AlbumViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()

    private val trackAdapter = TrackAdapter(manager = TrackManager())

    private lateinit var mediaPlayerManager: MediaPlayerManager

    private val args by navArgs<AlbumFragmentArgs>()

    override fun onCreateView() = R.layout.fragment_album

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sRefreshTracks.isEnabled = false

        with(rViewTracks) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = trackAdapter
            setHasFixedSize(true)
        }

        with(viewModel) {
            observe(tracks, ::tracksObserver)
            observe(download, ::downloadObserver)

            lookupTracks(albumId = args.albumId)

            sRefreshTracks.setOnRefreshListener {
                lookupTracks(albumId = args.albumId)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mediaPlayerManager = MediaPlayerManager(requireContext())
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
                sRefreshTracks.isRefreshing = true
            }
            is Result.OnSuccess -> {
                sRefreshTracks.isRefreshing = false

                val tracks = result.value

                val items = tracks.map(Track::toTrackItem)

                trackAdapter.swapItems(new = items)
            }
            is Result.OnError -> {
                sRefreshTracks.isRefreshing = false

                noConnectionSnackBar(isNetworkAvailable = connectionManager.isNetworkAvailable())
            }
            else -> {
                sRefreshTracks.isRefreshing = false

                snackBar {
                    messageResource = R.string.snack_bar_unexpected_failure
                }
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
                noConnectionSnackBar(isNetworkAvailable = connectionManager.isNetworkAvailable())
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
            val destination = AlbumFragmentDirections.navToTrackPlayer(
                url = track.previewUrl,
                title = track.trackName
            )

            navigate(destination)
        }
    }
}
