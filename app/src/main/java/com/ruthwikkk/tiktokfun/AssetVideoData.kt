package com.ruthwikkk.tiktokfun

object AssetVideoData {
    private const val ASSET_PATH = "file:///android_asset"

    val waves = PlaybackItem(
        id = "1",
        mediaUri = "$ASSET_PATH/waves.mp4",
        previewImageUri = "$ASSET_PATH/preview_waves.png",
    )
    val christmas = PlaybackItem(
        id = "2",
        mediaUri = "$ASSET_PATH/christmas.mp4",
        previewImageUri = "$ASSET_PATH/preview_christmas.png",
    )
    val yellow = PlaybackItem(
        id = "3",
        mediaUri = "$ASSET_PATH/yellow.mp4",
        previewImageUri = "$ASSET_PATH/preview_yellow.png",
    )

    val ALL = listOf(waves, christmas, yellow)
}
