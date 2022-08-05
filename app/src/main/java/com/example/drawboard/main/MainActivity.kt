package com.example.drawboard.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.drawboard.R
import com.example.drawboard.adapter.ClickEvent
import com.example.drawboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var model: MainViewModel

    var isWritable = false
    var isReadable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clickEvent = ClickEvent()
        binding.model = model
        binding.lifecycleOwner = this
        binding.mDrawView = binding.drawView

        //检测是否有权限
        checkPermission()
        //请求权限
        requestMPermissions()

    }

    private fun requestMPermissions() {
        //保存需要请求的权限
        val permissionArray = arrayListOf<String>()
        //创建请求权限的对象
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                //获取请求结果
                isReadable = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
                isWritable = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

                if (isWritable && isReadable) {
                    //将试图转化为图片然后保存图片

                }
            }

        if (!isReadable) {
            permissionArray.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isReadable) {
            permissionArray.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        //发起请求
        resultLauncher.launch(permissionArray.toTypedArray())
    }

    private fun checkPermission() {
        isReadable = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


        isWritable = if (Build.VERSION.SDK_INT >= 29) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}