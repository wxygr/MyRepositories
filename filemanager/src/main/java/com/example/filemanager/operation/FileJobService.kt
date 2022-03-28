package com.example.filemanager.operation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.annotation.MainThread
import com.example.filemanager.utils.removeFirst
import okio.Path
import java.util.concurrent.Executors
import java.util.concurrent.Future

class FileJobService : Service() {

    private val executorService = Executors.newCachedThreadPool()

    private val runningJobs = mutableMapOf<FileJob, Future<*>>()

    override fun onCreate() {
        super.onCreate()

        instance = this

        while (pendingJobs.isNotEmpty()) {
            startJob(pendingJobs.removeFirst())
        }
    }

    private fun startJob(job: FileJob) {
        // Synchronize on runningJobs to prevent a job from removing itself before being added.
        synchronized(runningJobs) {
            val future = executorService.submit {
                job.runOn(this)
                synchronized(runningJobs) { runningJobs.remove(job) }
            }
            runningJobs[job] = future
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY


    private fun cancelJob(id: Int) {
        synchronized(runningJobs) {
            runningJobs.removeFirst { it.key.id == id }?.value?.cancel(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        instance = null

        synchronized(runningJobs) {
            while (runningJobs.isNotEmpty()) {
                runningJobs.removeFirst().value.cancel(true)
            }
        }
    }
    companion object {
        private var instance: FileJobService? = null

        private val pendingJobs = mutableListOf<FileJob>()

        @MainThread
        private fun startJob(job: FileJob, context: Context) {
            val instance = instance
            if (instance != null) {
                instance.startJob(job)
            } else {
                pendingJobs.add(job)
                context.startService(Intent(context, FileJobService::class.java))
            }
        }

        // 点击悬浮按钮，创建文件或文件夹
        fun create(path: Path, createDirectory: Boolean, context: Context) {
            startJob(CreateFileJob(path, createDirectory), context)
        }


        fun delete(paths: List<Path>, context: Context) {
            startJob(DeleteFileJob(paths), context)
        }


        fun rename(path: Path, newName: String, context: Context) {
            startJob(RenameFileJob(path, newName), context)
        }

        // 点击粘贴后，copy文件/文件夹到指定路径
//        fun copy(sources: List<Path>, targetDirectory: Path, context: Context) {
//            startJob(CopyFileJob(sources, targetDirectory), context)
//        }




        @MainThread
        fun cancelJob(id: Int) {
            pendingJobs.removeFirst { it.id == id }
            instance?.cancelJob(id)
        }
    }
}