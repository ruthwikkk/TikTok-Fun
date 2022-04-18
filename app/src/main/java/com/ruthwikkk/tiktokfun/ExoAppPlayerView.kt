package com.ruthwikkk.tiktokfun

import android.view.LayoutInflater
import com.google.android.exoplayer2.Player
import com.ruthwikkk.tiktokfun.databinding.PlayerViewBinding

class ExoAppPlayerView(layoutInflater: LayoutInflater)  {
    private val binding = PlayerViewBinding.inflate(layoutInflater)
    val view = binding.root

    fun attach(appPlayer: Player) {
        binding.playerView.player = appPlayer
    }

    fun detachPlayer() {
        binding.playerView.player = null
    }
}
