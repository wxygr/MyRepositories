package com.example.materialdesign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_text_input_layout).setOnClickListener{
            startActivity(Intent(this, TextInputLayoutActivity::class.java))
        }
        findViewById<Button>(R.id.btn_navigation_view).setOnClickListener{
            startActivity(Intent(this, NavigationViewActivity::class.java))
        }
    }
}