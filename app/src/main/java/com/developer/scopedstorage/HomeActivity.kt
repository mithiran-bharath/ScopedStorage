package com.developer.scopedstorage

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class HomeActivity : AppCompatActivity() {


    private val TAG = "HomeActivity"

    private lateinit var bannerImage: AppCompatImageView
    private lateinit var btnCapture: AppCompatImageButton
    private lateinit var btnChooseGallery: AppCompatImageButton

    private var readPermissionGranted = false
    private var writePermissionGranted = false

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var latestTmpUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bannerImage = findViewById(R.id.imgBanner)
        btnCapture = findViewById(R.id.btnCaptureImage)
        btnChooseGallery = findViewById(R.id.btnPickImageFromGallery)

        permissionLauncher=
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted

            }

        updateOrRequestPermission()
        btnCapture.setOnClickListener {
            takeImage()
        }
        btnChooseGallery.setOnClickListener {
            selectImageFromGallery()
        }


    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    
    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d(TAG, "Photo Path: ${it.path}")
            bannerImage.setImageURI(uri)
        }
    }

    private fun takeImage() {
        getTempFileUri().let { uri ->

            latestTmpUri = uri
            takePicture.launch(uri)
        }
    }
    private fun getTempFileUri():Uri {
        val tempFile = File.createTempFile(
            "BHAR_${System.currentTimeMillis()}",
            ".png",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.provider",
            tempFile
        )
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->

        if(isSuccess) {

            latestTmpUri.let {
                Log.d(TAG, "Photo Path: ${it?.path}")
                bannerImage.setImageURI(it)
            }
        }

    }

    private fun updateOrRequestPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()

        if (!writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}