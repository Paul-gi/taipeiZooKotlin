package com.example.taipeizookotlin.textviewSet

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan

class RePlacementSpan : ReplacementSpan() {
    var mWidth: Int = 0
    var mPaint: Paint? = Paint()
    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        // return text with relative to the Paint
        mWidth = paint.measureText(text, start, end).toInt()
        return this.mWidth
    }

    @SuppressLint("ResourceAsColor")
    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {

        mPaint?.let {
            it.strokeWidth = 4f
            it.style = Paint.Style.STROKE
            it.color = Color.BLUE
            canvas.drawRect(
                x, 1 + top.toFloat(), x + mWidth.toFloat(), bottom.toFloat(),
                it
            )
        }
    }

}