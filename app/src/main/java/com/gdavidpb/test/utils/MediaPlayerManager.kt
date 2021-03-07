package com.gdavidpb.test.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.io.File
import java.util.concurrent.atomic.AtomicReference

class MediaPlayerManager(
    private val lifecycleOwner: LifecycleOwner
) {
    private val mediaPlayer = AtomicReference<ManagedMediaPlayer>()

    init {
        lifecycleOwner.lifecycle.addObserver(MediaPlayerObserver())
    }

    fun play(source: File, onComplete: () -> Unit) {
        mediaPlayer.get().play(source, onComplete)
    }

    fun pause() {
        mediaPlayer.get().pause()
    }

    fun stop() {
        mediaPlayer.get().stop()
    }

    private inner class MediaPlayerObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            mediaPlayer.set(ManagedMediaPlayer(lifecycleOwner))
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            mediaPlayer.get().release()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            mediaPlayer.get().release()
        }
    }
}