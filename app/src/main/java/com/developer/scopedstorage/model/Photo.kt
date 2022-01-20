package com.developer.multipartapplication.model

import android.net.Uri

data class Photo(
    val id:Long,
    val name:String,
    val width:Int,
    val height:Int,
    val uri: Uri,
    val isExternal :Boolean
)
