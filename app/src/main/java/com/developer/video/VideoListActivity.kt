package com.developer.video

import android.content.ContentUris
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.developer.scopedstorage.databinding.ActivityVideoListBinding
import com.developer.video.model.VideoModel
import java.util.concurrent.TimeUnit

class VideoListActivity : AppCompatActivity() {


    private val TAG = "VideoListActivity"
    private lateinit var binding: ActivityVideoListBinding
    private lateinit var mAdapter:VideoAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = VideoAdapter { position, video ->
            Log.d(TAG, "onCreate: $position  ${video.name}")
            Toast.makeText(this, video.name, Toast.LENGTH_SHORT).show()
        }

        binding.rvVideosList.adapter = mAdapter

        val videoList = getVideoList()

        if(videoList.isNotEmpty()) {
            mAdapter.submitList(videoList)
        }
        Log.d(TAG, "onCreate: $videoList")

        Toast.makeText(this, videoList.toString(), Toast.LENGTH_SHORT).show()
    }


    private fun getVideoList(): List<VideoModel> {

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
        )
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArg = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString()
        )
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            selection,
            selectionArg,
            sortOrder
        )
        val videos = mutableListOf<VideoModel>()

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)



            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val duration = cursor.getInt(durationColumn)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    videos += VideoModel(
                        name = name,
                        size = size,
                        duration = duration,
                        uri = uri,
                        thumbnails = contentResolver.loadThumbnail(uri, Size(640, 480), null)
                    )
                } else {
                    videos += VideoModel(
                        name = name,
                        size = size,
                        duration = duration,
                        uri = uri,
                        thumbnails = null
                    )
                }
            }

            if (!query.isClosed) {
                query.close()
            }


        }

        return videos

    }

}