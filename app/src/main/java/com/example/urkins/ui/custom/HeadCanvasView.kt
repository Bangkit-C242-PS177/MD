package com.example.urkins.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class HeadCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#FFB0C4")
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val pathHead = Path()
        val pathLeftEar = Path()
        val pathRightEar = Path()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 4f

        pathHead.addOval(centerX - radius, centerY - radius * 1.8f, centerX + radius, centerY + radius * 0.8f, Path.Direction.CW)
        pathLeftEar.addCircle(centerX - radius * 0.9f, centerY - radius * 0.5f, radius * 0.3f, Path.Direction.CW)
        pathRightEar.addCircle(centerX + radius * 0.9f, centerY - radius * 0.5f, radius * 0.3f, Path.Direction.CW)

        pathHead.op(pathLeftEar, Path.Op.UNION)
        pathHead.op(pathRightEar, Path.Op.UNION)

        canvas.drawPath(pathHead, paint)
    }
}