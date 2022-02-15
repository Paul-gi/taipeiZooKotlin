package com.example.taipeizookotlin.textviewSet

import android.text.TextPaint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader

import android.text.style.UpdateAppearance

import android.text.style.CharacterStyle


class RainbowSpan(context: Context) : CharacterStyle(),
    UpdateAppearance {

    private val colors: IntArray = context.resources.getIntArray(com.example.taipeizookotlin.R.array.rainbow)
    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, paint.textSize * colors.size, colors, null,
            Shader.TileMode.MIRROR
        )
        val matrix = Matrix()
        matrix.setRotate(90f)
        shader.setLocalMatrix(matrix)
        paint.shader = shader
    }

}