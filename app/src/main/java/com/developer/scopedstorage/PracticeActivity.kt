package com.developer.scopedstorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.developer.scopedstorage.databinding.ActivityPraticeBinding
import java.io.File
import kotlin.random.Random


class PracticeActivity : AppCompatActivity() {

    private val TAG = "PracticeActivity"

    private lateinit var binding: ActivityPraticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateFile.setOnClickListener {
            createFile("myFile_${Random.nextInt()}","Hello Bharath! New file created successfully.")
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show()
        }

        binding.btnReadFile.setOnClickListener {
            readFileUsingFileName("myFile")
        }

        binding.btnGetFiles.setOnClickListener {
            getFileList()
        }

        binding.btnCheckExternalStorage.setOnClickListener {
            Log.d(TAG, "onCreate: ${isExternalStorageIsReadable()}")
            Log.d(TAG, "onCreate: ${isExternalStorageIsWritable()}")
            chooseExternalStorageAsInternalMemory()
        }

    }

    /**
     *  @param fileName Describes the name of the file
     *  @param fileContent Describes the content of the file
     *  @see openFileOutput
     */
    private fun createFile(fileName:String, fileContent:String) {
       openFileOutput(fileName, Context.MODE_PRIVATE).use { fileOutputStream ->
            fileOutputStream.write(fileContent.toByteArray())
        }
    }

    /**
     * @param fileName Describes the name of the file
     * @see openFileInput
     * @see useLines
     * @see fold It works likes x=x+1
     */
    private fun readFileUsingFileName(fileName: String) {
        val result = openFileInput(fileName).bufferedReader().useLines { lines ->
            lines.fold("")  { accumulator, text ->
                "$accumulator\n$text"
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    private fun getFileList() {
        val files = fileList()
        Log.d(TAG, "getFileList: ${files.size}")
        for (file in files) {
            Log.d(TAG, "getFileList: $file")
        }
    }

    private fun isExternalStorageIsWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageIsReadable(): Boolean {
        return Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED,Environment.MEDIA_MOUNTED_READ_ONLY)
    }


    /**
     * Note: If your app is used on a device that runs Android 4.3 (API level 18) or lower,
     then the array contains just one element, which represents the primary external storage volume.
     */
    private fun chooseExternalStorageAsInternalMemory() {
        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext,null)
        val primaryExternalStorage = externalStorageVolumes[0]
        Log.d(TAG, "chooseExternalStorageAsInternalMemory: $primaryExternalStorage")
        Log.d(TAG, "chooseExternalStorageAsInternalMemory: ${externalStorageVolumes.size}")
    }


}