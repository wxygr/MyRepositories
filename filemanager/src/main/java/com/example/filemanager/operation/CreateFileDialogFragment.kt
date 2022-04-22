package com.example.filemanager.operation

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.filemanager.R
import com.example.filemanager.utils.show

class CreateFileDialogFragment : FileNameDialogFragment() {
    override val listener: Listener
        get() = requireParentFragment() as Listener

    @StringRes
    override val titleRes: Int = R.string.file_create_file_title

    override fun onOk(name: String) {
        listener.createFile(name)
    }

    companion object {
        fun show(fragment: Fragment) {
            CreateFileDialogFragment().show(fragment)
        }
    }

    interface Listener : FileNameDialogFragment.Listener {
        fun createFile(name: String)
    }
}
