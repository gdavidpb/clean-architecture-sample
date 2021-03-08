package com.gdavidpb.test.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.response.GetTrackPreviewResponse
import com.gdavidpb.test.domain.usecase.coroutines.Result
import com.gdavidpb.test.domain.usecase.errors.GetTrackPreviewError
import com.gdavidpb.test.domain.usecase.errors.LookupTracksError
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.presentation.viewmodel.AlbumViewModel
import com.gdavidpb.test.ui.adapters.TrackAdapter
import com.gdavidpb.test.utils.MediaPlayerManager
import com.gdavidpb.test.utils.extensions.observe
import com.gdavidpb.test.utils.mappers.toTrackItem
import kotlinx.android.synthetic.main.fragment_album.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : NavigationFragment() {

    private val viewModel: AlbumViewModel by viewModel()

    private val trackAdapter = TrackAdapter(manager = TrackManager())

    private val managedMediaPlayer by lazy {
        MediaPlayerManager(lifecycleOwner = this)
    }

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
            observe(preview, ::onTrackPreviewGotten)

            lookupTracks(albumId = args.albumId)

            sRefreshTracks.setOnRefreshListener {
                lookupTracks(albumId = args.albumId)
            }
        }
    }

    override fun onPause() {
        trackAdapter.resetStates()

        super.onPause()
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

    private fun onTrackPreviewGotten(result: Result<GetTrackPreviewResponse, GetTrackPreviewError>?) {
        when (result) {
            is Result.OnLoading -> {
            }
            is Result.OnSuccess -> handleOnTrackPreviewGottenSuccess(response = result.value)
            is Result.OnError -> handleOnTrackPreviewGottenError(error = result.error)
            else -> defaultErrorSnackBar()
        }
    }

    private fun handleOnGetTracksSuccess(tracks: List<Track>) {
        sRefreshTracks.isRefreshing = false

        val items = tracks.map { it.toTrackItem() }

        trackAdapter.submitList(items)
    }

    private fun handleOnGetTracksError(error: LookupTracksError?) {
        sRefreshTracks.isRefreshing = false

        when (error) {
            is LookupTracksError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = error.isNetworkAvailable)
            else -> defaultErrorSnackBar()
        }
    }

    private fun handleOnTrackPreviewGottenSuccess(response: GetTrackPreviewResponse) {
        val item = response.item

        managedMediaPlayer.play(
            source = response.trackFile,
            onStart = { onTrackStarted(item) },
            onComplete = { onTrackCompleted(item) }
        )
    }

    private fun handleOnTrackPreviewGottenError(error: GetTrackPreviewError?) {
        when (error) {
            is GetTrackPreviewError.NoConnection -> noConnectionSnackBar(isNetworkAvailable = error.isNetworkAvailable)
            else -> defaultErrorSnackBar()
        }
    }

    private fun onTrackStarted(item: TrackItem) {
        trackAdapter.updateItem(item) {
            copy(
                isLoading = false,
                isPlaying = true,
                isPaused = false
            )
        }
    }

    private fun onTrackCompleted(item: TrackItem) {
        trackAdapter.updateItem(item) {
            copy(
                isPlaying = false,
                isPaused = false
            )
        }
    }

    inner class TrackManager : TrackAdapter.AdapterManager {
        override fun onPlayTrackClicked(item: TrackItem) {
            managedMediaPlayer.stop()

            trackAdapter.updateItem(item) {
                copy(
                    isPlaying = false,
                    isPaused = false,
                    isLoading = true
                )
            }

            viewModel.getTrackPreview(item)
        }

        override fun onPauseTrackClicked(item: TrackItem) {
            managedMediaPlayer.pause()

            trackAdapter.updateItem(item) {
                copy(
                    isPlaying = false,
                    isPaused = true,
                    isLoading = false
                )
            }
        }

        override fun onPreviewTrackClicked(item: TrackItem) {
            val destination = AlbumFragmentDirections.navToPreview(
                url = item.previewUrl.toString(),
                title = item.trackName.toString()
            )

            navigate(destination)
        }
    }
}
