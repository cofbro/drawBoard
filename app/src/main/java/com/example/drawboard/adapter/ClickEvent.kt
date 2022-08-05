package com.example.drawboard.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.drawboard.DrawView
import com.example.drawboard.checkReadablePermission
import com.example.drawboard.colorsArray
import com.example.drawboard.main.MainActivity
import com.example.drawboard.main.MainViewModel
import java.io.OutputStream
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

class ClickEvent {

    fun clickDrawBtn(view: View, model: MainViewModel) {
        model.isDrawing.postValue(!model.isDrawing.value!!)
    }

    fun clickOnFloatingBtn(view: View, model: MainViewModel) {
        model.toList.postValue(!model.toList.value!!)
    }

    fun changePaintColor(view: View, drawView: DrawView) {
        val index = (view.tag as String).toInt() - 1
        val color = colorsArray[index]
        drawView.mPaint.color = Color.parseColor(color)
    }

    fun moveLast(view: View, drawView: DrawView) {
        if (drawView.pathModelList.isNotEmpty()) {
            drawView.pathModelList.removeLast()
        }
        drawView.invalidate()
    }

    fun savePictureToGallery(view: View, drawView: DrawView, model: MainViewModel) {
        val readPermission = view.context.checkReadablePermission()
        if (readPermission) {
            val bitmap = convertViewToBitmap(drawView)
            saveBitmap(bitmap, drawView) {
                if (it != null) {
                    model.imageUri = it
                }
            }
        } else {
            Toast.makeText(view.context, "没有权限，请在设置中授权", Toast.LENGTH_LONG).show()
        }
    }

    //将图片保存到相册中
    private fun saveBitmap(bitmap: Bitmap, drawView: DrawView, callback: (Uri?) -> Unit) {
        val activity = drawView.context as MainActivity

        //图片所在的文件夹的路径
        val uri = if (Build.VERSION.SDK_INT >= 29) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        //配置图片信息
        val contentValues = ContentValues().apply {
            //用时间作为文件名
            put(MediaStore.Images.Media.DISPLAY_NAME, getName())
            //图片格式
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            //宽度
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }
        //定位到这个图片的文件路径
        val imageUri = activity.contentResolver.insert(uri, contentValues)

        if (imageUri != null) {
            var imageOOS: OutputStream? = null
            try {
                //创建输出流
                imageOOS = activity.contentResolver.openOutputStream(imageUri)
                //通过输出流将bitmap写出
                val result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOOS)
                if (result) {
                    callback(imageUri)
                    Toast.makeText(drawView.context, "保存成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(drawView.context, "保存失败", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                imageOOS?.close()
            }
        } else {
            throw NullPointerException("获取路径失败")
        }


    }

    //将view的内容重新绘制到我们提供的bitmap上
    private fun convertViewToBitmap(view: DrawView): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#fafafa"))
        view.draw(canvas)
        return bitmap
    }

    //将时间转化为名字
    @SuppressLint("SimpleDateFormat")
    fun getName(): String {
        val simpleStr = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$simpleStr.jpg"
    }


    fun shareImage(view: View, model: MainViewModel) {
        if (model.imageUri != null) {
            //隐式跳转
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, model.imageUri)
                putExtra(Intent.EXTRA_TEXT, "我的靓照")
                type = "image/jpg"
            }
            view.context.startActivity(intent)
        } else {
            Toast.makeText(view.context, "尚未绘制图片", Toast.LENGTH_LONG).show()
        }

    }
}