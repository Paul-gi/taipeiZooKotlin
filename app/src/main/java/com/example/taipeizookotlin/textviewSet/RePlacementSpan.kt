package com.example.taipeizookotlin.textviewSet

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

class RePlacementSpan : ReplacementSpan() {
     var mWidth: Int = 0
     var mPaint: Paint? = null
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
        // draw the frame with custom Paint
        mPaint?.let {
            canvas.drawRect(x, top.toFloat(), x + mWidth.toFloat(), bottom.toFloat(),
                it
            )
        }
    }

}