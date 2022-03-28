/*
 * Copyright (c) 2020 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package com.example.filemanager.utils

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor


fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        mainExecutorCompat.execute { showToast(text, duration) }
        return
    }
    Toast.makeText(this, text, duration).show()
}

val Context.mainExecutorCompat: Executor
    get() = ContextCompat.getMainExecutor(this)


val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)