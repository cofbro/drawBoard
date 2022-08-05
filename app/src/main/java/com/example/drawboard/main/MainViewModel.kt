package com.example.drawboard.main

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var isDrawing = MutableLiveData(false)
    var toList = MutableLiveData(false)
    var imageUri: Uri? = null
}