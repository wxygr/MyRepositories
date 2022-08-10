package com.example.location

import android.Manifest
import androidx.activity.viewModels
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val vm: LocationViewModel by viewModels()

    private val permissionList = mutableListOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ensureLocationAccess()
    }

    private fun ensureLocationAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasLocationPermission()) {
                // todo: 获取地理位置，加载数据
                vm.location.observe(this) {
                    Log.i("wxy", "latitude: ${it?.latitude}  altitude: ${it?.altitude}")
                }
            } else {
                // todo: 弹出权限申请弹窗，点击弹窗的 next 后申请权限
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        PermissionHelper.with(this)
            .permissions(permissionList)
            .onGranted{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // todo：申请后台位置权限
                }
                // todo：判断 GPS 是否打开；判断是否有网络
                // todo：获取地理位置，加载数据
                vm.location.observe(this) {
                    Log.i("wxy", "latitude: ${it?.latitude}  altitude: ${it?.altitude}")
                }
            }
            .onDenied{
                // todo: 展示申请权限的弹窗
            }
            .request()
    }

}