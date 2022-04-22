package com.example.filemanager

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filemanager.operation.FileJobService
import com.permissionx.guolindev.PermissionX
import okio.*
import okio.Path.Companion.toPath
import java.io.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val path1Str = Environment.getExternalStorageDirectory().toString() + "/temp1"
    private val path2Str = Environment.getExternalStorageDirectory().toString() + "/temp2"
    private val nioFileSystem = FileSystem.SYSTEM
    private val path1 = path1Str.toPath()
    private val path2 = path2Str.toPath()
    private val TAG = "wxy"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handlePermission()
        initView()
        Log.i(TAG, "path1.parent: ${path1.parent}")
        val segments = path1.segments
        for (segment in segments) {
            Log.i(TAG, "segment: $segment")
        }
    }

    private fun initView() {
        // 创建文件夹
        findViewById<Button>(R.id.btn_create_folder).setOnClickListener {
            createFolder()
        }

        // 创建文件夹
        findViewById<Button>(R.id.btn_create_file).setOnClickListener {
            createFile()
        }

        // 删除
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            delete()
        }

        // 重命名
        findViewById<Button>(R.id.btn_rename).setOnClickListener {
            rename()
        }


        // 遍历temp1中的文件。复制到temp2，如果temp1中存在文件夹，则会抛出异常
        findViewById<Button>(R.id.btn_copy).setOnClickListener {
            if (nioFileSystem.exists(path1)) {
                val listPaths = nioFileSystem.list(path1)
                for (path in listPaths) {
                    copyFileUseOkio(path, "path2Str+\"/\"+path.name".toPath())
                }
            }
        }

        // 移动temp1文件夹到temp2文件夹
        findViewById<Button>(R.id.btn_move).setOnClickListener {
            nioFileSystem.atomicMove(path1, (path2Str + "/" + path1.name).toPath())
        }


        findViewById<Button>(R.id.btn_list).setOnClickListener {
            val list = nioFileSystem.list(path1) // 包含隐藏的文件夹/文件
            for (path in list) {
                Log.i(TAG, "list: $path")
            }
            val listRecursively = nioFileSystem.listRecursively(path2) //递归遍历
            for (path in listRecursively) {
                Log.i(TAG, "listRecursively: $path")
            }
        }
    }

    private fun createFolder() {
        FileJobService.create("$path2Str/folder".toPath(), true, this)
    }

    private fun createFile() {
        FileJobService.create("$path2Str/file2".toPath(), false, this)
    }

    private fun delete() {
        val list = mutableListOf<Path>()
        list.add(path1)
        FileJobService.delete(list, this)
    }

    private fun rename() {
        FileJobService.rename(("$path2Str/dffk").toPath(), "temp4", this)
    }


    private fun copyFileUseOkio(source: Path, target: Path) {
        try {
            nioFileSystem.copy(source, target) //只能对文件进行复制操作，包含隐藏文件
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "copy failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handlePermission() {
        if (!PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request { _, _, _ ->
                }

        }
    }

    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    private fun copy(fromFile: String, toFile: String): Boolean {
        //要复制的文件目录
        val root = File(fromFile)
        Log.i("xxxxxxxxx", "copy:$root ")
        val allFilePath = getAllFilePath(root)
        if (allFilePath.isNotEmpty()) {
            //目标目录
            val targetDir = File(toFile)
            //建立目录
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            //遍历要复制该目录下的所有文件
            for (i in allFilePath.indices) {
                copySdcardFile(allFilePath[i].absolutePath, toFile + "/" + allFilePath[i].name)
            }
            return true
        }
        return false
    }


    fun copy2(fromFile: String?, toFile: String): Boolean {
        //要复制的文件目录
        val currentFiles: Array<File>
        val root = File(fromFile)
        if (!root.exists()) {
            return false
        }
        //若是存在则获取当前目录下的所有文件 填充数组
        currentFiles = root.listFiles()
        //目标目录
        val targetDir = File(toFile)
        //建立目录
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        //遍历要复制该目录下的所有文件
        for (i in currentFiles.indices) {
            if (currentFiles[i].isDirectory) { //若是当前项为子目录 进行递归{
                copy(currentFiles[i].path, toFile + "/" + currentFiles[i].name)
            } else { //若是当前项为文件则进行文件拷贝
                copySdcardFile(currentFiles[i].path, toFile + "/" + currentFiles[i].name)
            }
        }
        return true
    }


    /**
     * 获取源文件下所有文件的绝对路径,存放在list中
     * @param file 源文件
     * @return 返回值代表源文件下所有文件的绝对路径
     */
    private fun getAllFilePath(file: File): List<File> {
        val lists = ArrayList<File>()
        if (!file.exists()) {
            return lists
        }
        if (file.isDirectory) {//如果文件是目录
            val files = file.listFiles()//把目录下所有目录和文件存在数组中
            if (files != null) {//该目录非空
                for (f in files) {//遍历数组内所有对象
                    if (f.isDirectory) {//如果还是目录
                        val element = getAllFilePath(f)//调用自身方法循环
                        lists.addAll(element)//将每一个子目录文件夹添加到list集合中
                    } else {//如果遍历出来是文件
                        lists.add(f)//将文件路径存入list集合
                    }
                }
            } else {//如果目录为空
                lists.add(file)//将空目录路径添加到list集合
            }
        } else {//如果传进来的file对象是文件
            lists.add(file)//将文件路径添加到list集合
        }
        return lists
    }

    //要复制的目录下的全部非子目录(文件夹)文件拷贝
    private fun copySdcardFile(fromFile: String?, toFile: String?): Boolean {
        return try {
            val fosfrom: InputStream = FileInputStream(fromFile)
            val fosto: OutputStream = FileOutputStream(toFile)
            val bt = ByteArray(1024)
            var c: Int
            while (fosfrom.read(bt).also { c = it } > 0) {
                fosto.write(bt, 0, c)
            }
            fosfrom.close()
            fosto.close()
            true
        } catch (ex: Exception) {
            false
        }
    }

    /*
    *删除文件夹
    */
    fun delete(dir: String): Boolean {
        // 若是dir不以文件分隔符结尾，自动添加文件分隔符
        var dir = dir
        if (!dir.endsWith(File.separator)) dir += File.separator
        val dirFile = File(dir)
        // 若是dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory) {
            Log.e("FileUtils", "删除目录失败：" + dir + "不存在！")
            return false
        }
        var flag = true
        // 删除文件夹中的全部文件包括子目录
        val files = dirFile.listFiles()
        for (i in files.indices) {
            // 删除子文件
            if (files[i].isFile) {
                flag = files[i].delete()
                if (!flag) break
            } else if (files[i].isDirectory) {
                flag = delete(
                    files[i]
                        .absolutePath
                )
                if (!flag) break
            }
        }
        if (!flag) {
            Log.e("FileUtils", "删除目录失败！")
            return false
        }
        // 删除当前目录
        return if (dirFile.delete()) {
            Log.e("FileUtils", "删除目录" + dir + "成功！")
            true
        } else {
            false
        }
    }


    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    private fun copy1(fromFile: String, toFile: String): Boolean {
        //要复制的文件目录
        val root = File(fromFile)
        Log.i("xxxxxxxxx", "copy:$root ")
        if (!root.exists()) {
            return false
        }
        if (root.isDirectory) {
            //若是存在则获取当前目录下的所有文件 填充数组
            val currentFiles = root.listFiles()
            if (currentFiles != null) {
                //目标目录
                val targetDir = File(toFile)
                //建立目录
                if (!targetDir.exists()) {
                    targetDir.mkdirs()
                }
                //遍历要复制该目录下的所有文件
                for (i in currentFiles.indices) {
                    if (currentFiles[i].isDirectory) { //若是当前项为子目录 进行递归{
                        copy(currentFiles[i].path, toFile + "/" + currentFiles[i].name)
                    } else { //若是当前项为文件则进行文件拷贝
                        copySdcardFile(currentFiles[i].path, toFile + "/" + currentFiles[i].name)
                    }
                }
                return true
            } else {
                return File(toFile + "/" + root.name).mkdirs()
            }
        } else {
            return copySdcardFile(root.path, toFile + "/" + root.name)
        }
    }
}