package com.developer.scopedstorage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContract


class PickDocument : ActivityResultContract<String, Uri>() {

    override fun createIntent(context: Context, input: String): Intent {

        val uri = Environment.getExternalStorageState()

        return Intent(Intent.ACTION_PICK)
//            .setDataAndType(Intent.CATEGORY_OPENABLE)
            .setType(input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<Uri>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}