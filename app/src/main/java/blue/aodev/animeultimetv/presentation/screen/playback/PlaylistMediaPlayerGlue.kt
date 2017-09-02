package blue.aodev.animeultimetv.presentation.screen.playback

import android.app.Activity
import android.net.Uri
import android.support.v17.leanback.media.PlaybackGlue
import android.support.v17.leanback.media.PlaybackTransportControlGlue
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.PlaybackControlsRow
import blue.aodev.animeultimetv.domain.model.Playlist


class PlaylistMediaPlayerGlue(context: Activity, impl: ExoPlayerAdapter, var playlist: Playlist)
    : PlaybackTransportControlGlue<ExoPlayerAdapter>(context, impl) {

    private val previousAction = PlaybackControlsRow.SkipPreviousAction(context)
    private val nextAction = PlaybackControlsRow.SkipNextAction(context)

    init {
        updateCurrentVideo()
        playWhenReady()
    }

    private fun updateCurrentVideo() {
        val currentVideo = playlist.currentVideo
        title = playlist.title
        subtitle = currentVideo.title
        playerAdapter.setDataSource(Uri.parse(currentVideo.hdVideoUrl))
        NetworkPlaybackSeekDataProvider.setDemoSeekProvider(this, currentVideo.hdVideoUrl)
    }

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        super.onCreatePrimaryActions(adapter)
        adapter.add(previousAction)
        adapter.add(nextAction)
    }

    fun playWhenReady() {
        if (isPrepared) {
            play()
        } else {
            addPlayerCallback(object : PlayerCallback() {
                override fun onPreparedStateChanged(glue: PlaybackGlue) {
                    if (glue.isPrepared) {
                        glue.removePlayerCallback(this)
                        glue.play()
                    }
                }
            })
        }
    }

    override fun next() {
        if (playlist.hasNextVideo) {
            playlist = playlist.next()
            updateCurrentVideo()
            playWhenReady()
        }
    }

    override fun previous() {
        if (playlist.hasPreviousVideo) {
            playlist = playlist.previous()
            updateCurrentVideo()
            playWhenReady()
        }
    }

    override fun onPlayCompleted() {
        super.onPlayCompleted()
        next()
    }
}
