package com.example.filemanager.utils

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

fun TextView.hideTextInputLayoutErrorOnTextChange(textInputLayout: TextInputLayout) {
    doAfterTextChanged { textInputLayout.error = null }
}

/** @see com.android.keyguard.KeyguardPasswordView#onEditorAction */
fun TextView.setOnEditorConfirmActionListener(listener: (TextView) -> Unit) {
    setOnEditorActionListener { view, actionId, event ->
        val isConfirmAction = if (event != null) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER,
                KeyEvent.KEYCODE_NUMPAD_ENTER -> true
                else -> false
            } && event.action == KeyEvent.ACTION_DOWN
        } else {
            when (actionId) {
                EditorInfo.IME_NULL, EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT -> true
                else -> false
            }
        }
        if (isConfirmAction) {
            listener(view)
            true
        } else {
            false
        }
    }
}

fun DialogFragment.show(fragment: Fragment) {
    show(fragment.childFragmentManager, null)
}