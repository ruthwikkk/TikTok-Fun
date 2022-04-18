package com.ruthwikkk.tiktokfun

data class PlaybackItem(
    val id: String,
    val mediaUri: String,
    val previewImageUri: String,
    val aspectRatio: Float? = null
)
