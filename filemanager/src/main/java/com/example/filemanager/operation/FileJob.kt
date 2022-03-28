/*
 * Copyright (c) 2019 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package com.example.filemanager.operation

import com.example.filemanager.utils.showToast
import java.io.IOException
import java.io.InterruptedIOException
import java.util.Random

abstract class FileJob {
    val id = Random().nextInt()

    internal lateinit var service: FileJobService
        private set

    fun runOn(service: FileJobService) {
        this.service = service
        try {
            run()
            // TODO: Toast
        } catch (e: InterruptedIOException) {
            // TODO
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
            service.showToast(e.toString())
        } finally {
            // todo 终止服务
        }
    }

    @Throws(IOException::class)
    protected abstract fun run()
}
