package com.example.drawboard.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.databinding.BindingAdapter
import com.example.drawboard.CircleDraw


@SuppressLint("Recycle")
@BindingAdapter("showDetails")
fun CircleDraw.showDetails(isDrawing: Boolean) {
    if (isDrawing) {
        ObjectAnimator.ofFloat(this, "translationY", 0f, -height.toFloat() - 2).apply {
            duration = 500
            interpolator = LinearInterpolator()
        }.start()
    } else {
        ObjectAnimator.ofFloat(this, "translationY", -height.toFloat() - 2, 0f).apply {
            duration = 500
            interpolator = LinearInterpolator()
        }.start()
    }
}

@BindingAdapter("toList")
fun View.toList(toList: Boolean) {
    val num: Int = Integer.valueOf(tag.toString())
    if (toList) {
        ObjectAnimator.ofFloat(this, "translationY", 0f, -num * (height.toFloat() + 50f)).apply {
            duration = 350
            interpolator = BounceInterpolator()
        }.start()
    } else {
        ObjectAnimator.ofFloat(this, "translationY", -num * (height.toFloat() + 50f), 0f).apply {
            duration = 350
            interpolator = BounceInterpolator()
        }.start()
    }
}

