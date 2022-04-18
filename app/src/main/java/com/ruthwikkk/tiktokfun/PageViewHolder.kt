package com.ruthwikkk.tiktokfun

import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruthwikkk.tiktokfun.databinding.PageItemBinding

class PageViewHolder(private val binding: PageItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playbackItem: PlaybackItem) {
        Glide.with(binding.root.context).load(playbackItem.previewImageUri).into(binding.previewImage)

        ConstraintSet().apply {
            clone(binding.root)
            val ratio = playbackItem.aspectRatio?.let { "$it:1" }
            setDimensionRatio(binding.playerContainer.id, ratio)
            setDimensionRatio(binding.previewImage.id, ratio)
            applyTo(binding.root)
        }
    }

    fun attach(appPlayerView: ExoAppPlayerView) {
        if (binding.playerContainer == appPlayerView.view.parent) {
            // Already attached
            return
        }

        appPlayerView.view.findParentById(binding.root.id)
            ?.let(PageItemBinding::bind)
            ?.apply {
                playerContainer.removeView(appPlayerView.view)
                previewImage.isVisible = true
            }

        binding.playerContainer.addView(appPlayerView.view)
    }

    fun hidePreviewImage() {
        binding.previewImage.isVisible = false
    }
}