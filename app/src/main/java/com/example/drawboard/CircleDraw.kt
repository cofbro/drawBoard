package com.example.drawboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleDraw : View {
    private var space = 0f
    private var cx = 0f
    private var cy = 0f
    private var raduis = 0f
    private val mPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val bPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
    }
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        space = height.toFloat()
        cx = space
        cy = space / 2
        raduis = cy / 4 - 2
    }
    override fun onDraw(canvas: Canvas?) {
        for (i in 0..3) {
            canvas?.drawCircle(
                cx + width / 4 * i,
                cy,
                raduis * (i + 1),
                mPaint
            )
        }

        canvas?.drawLine(0f,height.toFloat(), width.toFloat(), height.toFloat(), bPaint)
    }
}