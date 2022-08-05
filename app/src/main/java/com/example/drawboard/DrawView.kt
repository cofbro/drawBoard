package com.example.drawboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView : View {

    var pathModelList = arrayListOf<PathModel>()
    private var mColor = Color.BLACK
        set(value) {
            field = value
            mPaint.color = value
        }
    var mStrokeWidth = this.context.dp2px(2)
    private var mPath : Path? = Path()
    var mPaint = Paint().apply {
        color = mColor
        strokeWidth = mStrokeWidth
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    override fun onDraw(canvas: Canvas?) {
        pathModelList.forEach {
            canvas?.drawPath(
                it.path, it.paint
            )

        }
        mPath?.let {
            canvas?.drawPath(mPath!!, mPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                //创建新的path
                mPath = Path()
                //确定落点
                mPath!!.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                //从path的最后一个点到当前点画一条线
                mPath!!.lineTo(event.x, event.y)
                //刷新
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                //保存当前路径
                pathModelList.add(
                    PathModel(Paint().apply {
                        color = mPaint.color
                        strokeWidth = mStrokeWidth
                        style = Paint.Style.STROKE
                        strokeCap = Paint.Cap.ROUND
                        strokeJoin = Paint.Join.ROUND
                    }, mPath!!)
                )
                mPath = null
            }
        }
        return true
    }

}