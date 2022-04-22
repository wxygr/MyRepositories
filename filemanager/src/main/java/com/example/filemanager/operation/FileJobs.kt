package com.example.filemanager.operation

import okio.FileSystem
import okio.Path
import java.io.File
import java.io.IOException
import java.io.InterruptedIOException

private val fileSystem = FileSystem.SYSTEM

@Throws(InterruptedIOException::class)
private fun throwIfInterrupted() {
    if (Thread.interrupted()) {
        throw InterruptedIOException()
    }
}

class CreateFileJob(private val path: Path, private val createDirectory: Boolean) : FileJob() {
    @Throws(IOException::class)
    override fun run() {
        create(path, createDirectory)
    }
}

@Throws(IOException::class)
private fun FileJob.create(path: Path, createDirectory: Boolean) {
    try {
        if (createDirectory) {
            fileSystem.createDirectory(path)
        } else {
//            path.toFile().createNewFile()
            fileSystem.write(path, true) {}
        }
    } catch (e: InterruptedIOException) {
        throw e
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

class DeleteFileJob(private val paths: List<Path>) : FileJob() {
    @Throws(IOException::class)
    override fun run() {
        for (path in paths) {
            fileSystem.deleteRecursively(path) //递归地删除文件夹/文件
            throwIfInterrupted()
        }
    }
}

class RenameFileJob(private val path: Path, private val newName: String) : FileJob() {
    @Throws(IOException::class)
    override fun run() {
        val newFile = File(path.parent.toString() + "/" + newName)
        path.toFile().renameTo(newFile) // 文件夹/文件都可
    }
}
