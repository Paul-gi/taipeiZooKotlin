package com.example.taipeizookotlin.textviewSet

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance


class AnimatedColorSpan(mContext: Context) : CharacterStyle(),
    UpdateAppearance {
    private val colors: IntArray = mContext.resources.getIntArray(com.example.taipeizookotlin.R.array.rainbow)
    private var shader: Shader? = null
    private val matrix: Matrix = Matrix()
    var translateXPercentage = 0f

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val width = paint.textSize * colors.size
        if (shader == null) {
            shader = LinearGradient(
                0f, 0f, 0f, width, colors, null,
                Shader.TileMode.MIRROR
            )
        }
        matrix.reset()
        matrix.setRotate(90f)
        matrix.postTranslate(width * translateXPercentage, 0f)
        shader!!.setLocalMatrix(matrix)
        paint.shader = shader
    }

}