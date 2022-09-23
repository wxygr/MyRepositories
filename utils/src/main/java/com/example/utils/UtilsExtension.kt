package com.example.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 获取屏幕宽度
 */
fun Context.getScreenWidth(): Int {
    val dm = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

/**
 * dp 转 px
 */
fun Int.dp2px(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
).toInt()

/**
 * 判断某个应用是否安装
 */
fun Context.isInstalled(packageName: String): Boolean =
    try {
        this.packageManager.getPackageInfo(packageName, 0) != null
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

/**
 * 浏览器打开该链接。
 * @param url 待打开的url。
 */
fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.parse(url)
    this.startActivity(intent)
}

/**
 * 跳转到系统设置中该应用的页面
 */
internal fun Context.getApplicationSettingIntent(): Intent = Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", this.applicationContext.packageName, null)
)

/**
 * 时间戳转换
 */
internal fun Long.convertToSpecificFormat(format: String, timeZone: String) =
    SimpleDateFormat(format, Locale.getDefault()).also {
        it.timeZone = TimeZone.getTimeZone(timeZone)
    }.format(this)

/**
 * 改变状态栏风格。
 * @param lite true for 浅色背景 false for 深色背景。
 */
fun AppCompatActivity.lightenStatusBar(lite: Boolean, view: View) {
    val controller = ViewCompat.getWindowInsetsController(view)
    setTransparentStatusBar()
    controller?.isAppearanceLightStatusBars = lite
}

/**
 * 设置透明状态栏。
 */
fun AppCompatActivity.setTransparentStatusBar() {
    if (Build.VERSION.SDK_INT >= 21) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= 19) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/**
 * 隐藏软键盘
 * @param context 当前上下文。
 * @param view 任意View。
 */
fun hideSoftInput(context: Context, view: View) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
