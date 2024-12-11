package com.example.urkins.ui.custom

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.urkins.R

class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    private var gradientColors = intArrayOf(
        ContextCompat.getColor(context, R.color.pink),
        ContextCompat.getColor(context, R.color.mint)
    )

    private var shader: Shader? = null

    override fun onDraw(canvas: android.graphics.Canvas) {
        paint.shader = shader
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shader = LinearGradient(0f, 0f, 0f, h.toFloat(), gradientColors, null, Shader.TileMode.CLAMP)
    }
}