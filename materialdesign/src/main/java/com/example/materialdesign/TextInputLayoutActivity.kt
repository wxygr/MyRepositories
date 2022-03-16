package com.example.materialdesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class TextInputLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input_layout)
        initView()
    }
    private fun initView() {
        var inputText = ""
        val textInputLayout = findViewById<TextInputLayout>(R.id.text_input_layout)
        val nameEt = findViewById<EditText>(R.id.et_name)
        nameEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                inputText = s.toString()
            }
        })
        val okBtn = findViewById<Button>(R.id.btn_ok)
        okBtn.setOnClickListener {
            if (inputText.length < 10) {
                textInputLayout.error = "名字长度输入太短"
            }
        }
    }
}