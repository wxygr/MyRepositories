package com.example.materialdesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.example.materialdesign.databinding.ActivityTextInputLayoutBinding
import com.google.android.material.textfield.TextInputLayout

/**
 * TextInputLayout 主要是作为 EditText 的容器，从而为 EditText 生成一个浮动的 Label.
 * 当用户点击 EditText 的时候，EditText 中的 hint 字符串会自动移到 EditText 的左上角。
 * 默认浮动标签样式跟 APP 主题色 colorAccent 色值一致，所以需要自定义样式，修改标签字体大小和颜色。
 * setError：自带错误提示方法，只是自定义提示文本内容。
 *
 */
class TextInputLayoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextInputLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextInputLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    private fun initView() {
        var inputText = ""
        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                inputText = s.toString()
            }
        })
        binding.btnOk.setOnClickListener {
            if (inputText.length < 10) {
                binding.textInputLayoutName.error = "名字长度输入太短"
            }
        }
    }
}