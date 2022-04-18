package com.ruthwikkk.tiktokfun

import kotlinx.coroutines.flow.Flow

interface VideoDataRepository {
    fun videoData(): Flow<List<PlaybackItem>>
}