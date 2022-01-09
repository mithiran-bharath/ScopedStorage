package com.developer.video.model

import android.net.Uri

data class VideoModel(
    val name: String,
    val duration: Int,
    val size: Int,
    val uri: Uri,
    val location: String ?= ""
)
