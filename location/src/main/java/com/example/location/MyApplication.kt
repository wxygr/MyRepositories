package com.example.location

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        lateinit var app: MyApplication
            private set
    }
}