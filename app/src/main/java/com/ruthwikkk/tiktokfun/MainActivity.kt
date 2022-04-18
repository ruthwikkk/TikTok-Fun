package com.ruthwikkk.tiktokfun

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashChunkSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.ruthwikkk.tiktokfun.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private val DASH_SAMPLE = "https://storage.googleapis.com/exoplayer-test-media-1/60fps/bbb-clear-1080/manifest.mpd"
    private lateinit var binding : ActivityMainBinding

    private var adapter: PageAdapter? = null
    private var appPlayerView: ExoAppPlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        appPlayerView = ExoAppPlayerView(layoutInflater)

        exoPlayer = ExoPlayer.Builder(this.applicationContext)
            //.setTrackSelector(trackSelector!!)
            //.setLoadControl(customLoadControl)
            .build()

        adapter = PageAdapter()

        binding.viewPager.adapter = adapter

        exoPlayer?.let { appPlayerView?.attach(it) }

        lifecycleScope.launchWhenResumed {
            RedditVideoDataRepository().videoData().collect {
                adapter?.submitList(it)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(callback)
    }

    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            adapter?.currentList?.get(position)?.let { playVideo(it, position) }

            lifecycleScope.launch {
                adapter?.showPlayerFor(position)
            }
        }
    }

    private fun playVideo(playbackItem: PlaybackItem, pos:Int){
        // exoPlayer?.setMediaSource(progressiveMediaSource(Uri.parse(url)))

        val mediaItem = MediaItem.Builder()
            .setMediaId(playbackItem.id)
            .setUri(Uri.parse(playbackItem.mediaUri))
            .build()

        exoPlayer?.addMediaItem(mediaItem)
        exoPlayer?.seekToDefaultPosition(pos)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true

        lifecycleScope.launch {
            appPlayerView?.let { adapter?.attachPlayerView(it, pos) }
        }
    }

    private fun buildDashMediaSource(uri: Uri): DashMediaSource {
        val manifestDataSourceFactory = DefaultDataSource.Factory(this)

        val dashChunkSourceFactory: DashChunkSource.Factory = DefaultDashChunkSource.Factory(
            DefaultDataSource.Factory(this)
        )

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()

        return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
            .createMediaSource(mediaItem)
    }

    private fun progressiveMediaSource(uri: Uri): ProgressiveMediaSource {
        val manifestDataSourceFactory = DefaultDataSource.Factory(this)

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()

        return ProgressiveMediaSource.Factory(manifestDataSourceFactory)
            .createMediaSource(mediaItem)
    }
}