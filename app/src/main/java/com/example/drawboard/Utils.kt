package com.example.drawboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.dp2px(dp: Int) = (resources.displayMetrics.density * dp)

var colorsArray = arrayOf("#fe5723", "#9b27ae", "#ea1e63", "#2095f4", "#fafafa")



//检测权限
fun Context.checkReadablePermission(): Boolean {
    val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    return result == PackageManager.PERMISSION_GRANTED
}