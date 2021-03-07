package com.gdavidpb.test.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.domain.usecase.errors.DownloadTrackError
import com.gdavidpb.test.domain.usecase.errors.LookupTracksError
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.presentation.viewmodel.AlbumViewModel
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.MediaPlayerManager
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.mappers.toTrack
import com.gdavidpb.test.utils.mappers.toTrackItem
import kotlinx.android.synthetic.main.fragment_album.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : NavigationFragment() {

    private val viewModel: AlbumViewModel by viewModel()

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
            observe(tracks, ::onGetTracks)
            observe(download, ::onTrackDownloaded)

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

    private fun onGetTracks(result: Result<List<Track>, LookupTracksError>?) {
        when (result) {
            is Result.OnLoading -> {
                sRefreshTracks.isRefreshing = true
            }
            is Result.OnSuccess -> {
                handleOnGetTracksSuccess(tracks = result.value)
            }
            is Result.OnError -> {
                handleOnGetTracksError(error = result.error)
            }
            else -> {
                sRefreshTracks.isRefreshing = false

                defaultErrorSnackBar()
            }
        }
    }

    private fun onTrackDownloaded(result: Result<DownloadTrackResponse, DownloadTrackError>?) {
        when (result) {
            is Result.OnSuccess -> handleOnTrackDownloadedSuccess(response = result.value)
            is Result.OnError -> handleOnTrackDownloadedError(error = result.error)
        }
    }

    private fun handleOnGetTracksSuccess(tracks: List<Track>) {
        sRefreshTracks.isRefreshing = false

        val items = tracks.map(Track::toTrackItem)

        trackAdapter.swapItems(new = items)
    }

    private fun handleOnGetTracksError(error: LookupTracksError?) {
        sRefreshTracks.isRefreshing = false

        when (error) {
            is LookupTracksError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = error.isNetworkAvailable)
            else -> defaultErrorSnackBar()
        }
    }

    private fun handleOnTrackDownloadedSuccess(response: DownloadTrackResponse) {
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

    private fun handleOnTrackDownloadedError(error: DownloadTrackError?) {
        when (error) {
            is DownloadTrackError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = error.isNetworkAvailable)
            else -> defaultErrorSnackBar()
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
