package cl.yapo.test.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.File

open class MediaPlayerManager(
    private val context: Context
) {
    private val mediaPlayer = MediaPlayer()

    private var currentSource = ""

    fun play(source: File, done: () -> Unit) {
        with(mediaPlayer) {
            when {
                currentSource == source.path && isPaused() -> start()
                else -> {
                    currentSource = source.path

                    reset()

                    setOnPreparedListener {
                        start()
                    }

                    setOnCompletionListener {
                        reset()

                        done()
                    }

                    setDataSource(context, Uri.fromFile(source))
                    prepareAsync()
                }
            }
        }
    }

    fun stop() {
        with(mediaPlayer) {
            if (isPlaying) stop()
        }
    }

    fun pause() {
        with(mediaPlayer) {
            if (isPlaying) pause()
        }
    }

    fun release() {
        mediaPlayer.release()
    }

    private fun MediaPlayer.isPaused(): Boolean {
        return !isPlaying && currentPosition > 0
    }
}