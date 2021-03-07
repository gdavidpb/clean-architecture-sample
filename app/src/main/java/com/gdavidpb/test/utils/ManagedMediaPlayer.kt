package com.gdavidpb.test.utils

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.util.concurrent.atomic.AtomicReference

class ManagedMediaPlayer(
    private val lifecycleOwner: LifecycleOwner
) : MediaPlayer() {

    private val dataSource = AtomicReference<String>()

    override fun isPlaying(): Boolean {
        return runCatching { super.isPlaying() }.getOrDefault(false)
    }

    override fun stop() {
        if (isPlaying) super.stop()
    }

    override fun pause() {
        if (isPlaying) super.pause()
    }

    override fun setDataSource(path: String) {
        dataSource.set(path)
        super.setDataSource(path)
    }

    fun play(source: File, onComplete: () -> Unit) {
        val isPlayingSame = dataSource.get() == source.path
        val isPaused = !isPlaying && currentPosition > 0

        if (isPlayingSame && isPaused)
            start()
        else {
            reset()

            setOnPreparedListener {
                lifecycleOwner.lifecycleScope.launchWhenResumed {
                    start()
                }
            }

            setOnCompletionListener {
                lifecycleOwner.lifecycleScope.launchWhenResumed {
                    onComplete()
                }
            }

            setDataSource(source.path)
            prepareAsync()
        }
    }
}