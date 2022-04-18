package com.ruthwikkk.tiktokfun

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AssetVideoDataRepository: VideoDataRepository {
    override fun videoData(): Flow<List<PlaybackItem>> {
        return flowOf(AssetVideoData.ALL)
    }
}
