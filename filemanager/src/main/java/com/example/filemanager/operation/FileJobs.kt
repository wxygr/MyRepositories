/*
 * Copyright (c) 2019 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package com.example.filemanager.operation

import okio.FileSystem
import okio.Path
import java.io.File
import java.io.IOException
import java.io.InterruptedIOException


private const val PROGRESS_INTERVAL_MILLIS = 200L
private val fileSystem = FileSystem.SYSTEM


@Throws(InterruptedIOException::class)
private fun FileJob.throwIfInterrupted() {
    if (Thread.interrupted()) {
        throw InterruptedIOException()
    }
}

//------------------------------------------------------------create------------------------------------------------------------------------------

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
            // todo:两种方式创建文件
//            path.toFile().createNewFile()
            fileSystem.write(path, true) {}
        }
    } catch (e: InterruptedIOException) {
        throw e
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

//------------------------------------------------------------create end------------------------------------------------------------------------------

//------------------------------------------------------------delete------------------------------------------------------------------------------

class DeleteFileJob(private val paths: List<Path>) : FileJob() {
    @Throws(IOException::class)
    override fun run() {
        for (path in paths) {
            fileSystem.deleteRecursively(path) //递归地删除文件夹/文件
            throwIfInterrupted()
        }
    }
}

//------------------------------------------------------------delete end------------------------------------------------------------------------------


//------------------------------------------------------------rename------------------------------------------------------------------------------

class RenameFileJob(private val path: Path, private val newName: String) : FileJob() {
    @Throws(IOException::class)
    override fun run() {
        val newFile = File(path.parent.toString() + "/" + newName)
        path.toFile().renameTo(newFile) // 文件夹/文件都可
    }
}
//------------------------------------------------------------rename end------------------------------------------------------------------------------


//------------------------------------------------------------copy ------------------------------------------------------------------------------
//class CopyFileJob(private val sources: List<Path>, private val targetDirectory: Path) : FileJob() {
//    @Throws(IOException::class)
//    override fun run() {
//        for (source in sources) {
//            val target = if (source.parent == targetDirectory) {
//                getTargetPathForDuplicate(source)
//            } else {
//                targetDirectory.resolveForeign(getTargetFileName(source))
//            }
//            copyRecursively(source, target)
//            throwIfInterrupted()
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun copyRecursively(source: Path, target: Path) {
//        Files.walkFileTree(
//            source,
//            object : SimpleFileVisitor<Path>() {
//                @Throws(IOException::class)
//                override fun preVisitDirectory(
//                    directory: Path,
//                    attributes: BasicFileAttributes
//                ): FileVisitResult {
//                    val directoryInTarget = target.resolveForeign(source.relativize(directory))
//                    val copied = copy(
//                        directory, directoryInTarget
//                    )
//                    throwIfInterrupted()
//                    return if (copied) FileVisitResult.CONTINUE else FileVisitResult.SKIP_SUBTREE
//                }
//
//                @Throws(IOException::class)
//                override fun visitFile(
//                    file: Path,
//                    attributes: BasicFileAttributes
//                ): FileVisitResult {
//                    val fileInTarget = target.resolveForeign(source.relativize(file))
//                    copy(file, fileInTarget)
//                    throwIfInterrupted()
//                    return FileVisitResult.CONTINUE
//                }
//
//                @Throws(IOException::class)
//                override fun visitFileFailed(file: Path, exception: IOException): FileVisitResult {
//                    // TODO: Prompt retry, skip, skip-all or abort.
//                    return super.visitFileFailed(file, exception)
//                }
//            }
//        )
//    }
//
//    private fun getTargetPathForDuplicate(source: Path): Path {
//        source.asByteStringListPath()
//        val sourceFileName = source.fileNameByteString!!
//        // We do want to follow symbolic links here.
//        val countEndIndex = if (source.isDirectory()) {
//            sourceFileName.length
//        } else {
//            sourceFileName.asFileName().baseName.length
//        }
//        val countInfo = getDuplicateCountInfo(sourceFileName, countEndIndex)
//        var i = countInfo.count + 1
//        while (i > 0) {
//            val targetFileName = setDuplicateCount(sourceFileName, countInfo, i)
//            val target = source.resolveSibling(targetFileName)
//            if (!target.exists(LinkOption.NOFOLLOW_LINKS)) {
//                return target
//            }
//            ++i
//        }
//        // Just leave it to conflict handling logic.
//        return source
//    }
//
//    private fun getDuplicateCountInfo(fileName: ByteString, countEnd: Int): DuplicateCountInfo {
//        while (true) {
//            // /(?<=.) \(\d+\)$/
//            var index = countEnd - 1
//            // \)
//            if (index < 0 || fileName[index] != ')'.code.toByte()) {
//                break
//            }
//            --index
//            // \d+
//            val digitsEndInclusive = index
//            while (index >= 0) {
//                val b = fileName[index]
//                if (b < '0'.code.toByte() || b > '9'.code.toByte()) {
//                    break
//                }
//                --index
//            }
//            if (index == digitsEndInclusive) {
//                break
//            }
//            val countString = fileName.substring(index + 1, digitsEndInclusive + 1).toString()
//            val count = try {
//                countString.toInt()
//            } catch (e: NumberFormatException) {
//                break
//            }
//            // \(
//            if (index < 0 || fileName[index] != '('.code.toByte()) {
//                break
//            }
//            --index
//            //
//            if (index < 0 || fileName[index] != ' '.code.toByte()) {
//                break
//            }
//            // (?<=.)
//            if (index == 0) {
//                break
//            }
//            return DuplicateCountInfo(index, countEnd, count)
//        }
//        return DuplicateCountInfo(countEnd, countEnd, 0)
//    }
//
//    private fun setDuplicateCount(
//        fileName: ByteString,
//        countInfo: DuplicateCountInfo,
//        count: Int
//    ): ByteString {
//        return ByteStringBuilder(fileName.substring(0, countInfo.countStart))
//            .append(" ($count)".toByteString())
//            .append(fileName.substring(countInfo.countEnd))
//            .toByteString()
//    }
//
//    private class DuplicateCountInfo(val countStart: Int, val countEnd: Int, val count: Int)
//}
//
//@Throws(IOException::class)
//private fun FileJob.copy(
//    source: Path,
//    target: Path,
//): Boolean =
//    copyOrMove(source, target, true, false)
////------------------------------------------------------------copy end ------------------------------------------------------------------------------
//
//
//// @see https://github.com/GNOME/nautilus/blob/master/src/nautilus-file-operations.c copy_move_file
//@Throws(IOException::class)
//private fun FileJob.copyOrMove(
//    source: Path,
//    target: Path,
//    useCopy: Boolean,
//    copyAttributes: Boolean,
//): Boolean {
//    val targetParent = target.parent
//    if (targetParent.startsWith(source) || source.startsWith(target)) {
//        // Don't allow copy/move into the source itself.
//        // Don't allow copy/move over the source itself or its ancestors.
//        // Well, we wouldn't want to copy when the target is inside the source otherwise it'll end into a loop
//        // todo:提示用户不能粘贴的路径不合理
//        return false
//    }
//    val targetTemp = target
//    var replaceExisting = false
//    var retry: Boolean
//    loop@ do {
//        retry = false
//        val options = mutableListOf<CopyOption>().apply {
//            this += LinkOption.NOFOLLOW_LINKS
//            if (copyAttributes) {
//                this += StandardCopyOption.COPY_ATTRIBUTES
//            }
//            if (replaceExisting) {
//                this += StandardCopyOption.REPLACE_EXISTING
//            }
//            this += ProgressCopyOption(PROGRESS_INTERVAL_MILLIS) {
//            }
//        }.toTypedArray()
//        try {
//            if (useCopy) {
//                source.copyTo(targetTemp, *options)
//            } else {
//                source.moveTo(targetTemp, *options)
//            }
//        } catch (e: FileAlreadyExistsException) {
//            return false
//        } catch (e: FileSystemException) {
//            // TODO: Prompt invalid name.
//            if (false) {
//                retry = true
//                continue
//            }
//            throw e
//        } catch (e: InterruptedIOException) {
//            throw e
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return false
//        }
//    } while (retry)
//    return true
//}
