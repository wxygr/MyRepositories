package com.example.network

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.network.java.MainActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_java).setOnClickListener {
            startActivity(
                Intent(
                    this@StartActivity,
                    MainActivity::class.java
                )
            )
        }

        findViewById<Button>(R.id.btn_kotlin).setOnClickListener {
            startActivity(
                Intent(
                    this@StartActivity,
                    com.example.network.kotlin.MainActivity::class.java
                )
            )
        }
    }
}