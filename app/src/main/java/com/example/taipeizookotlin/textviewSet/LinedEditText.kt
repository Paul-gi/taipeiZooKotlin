package com.example.taipeizookotlin.textviewSet

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.EditText


@SuppressLint("AppCompatCustomView")
class LinedEditText : EditText {
    private var paint: Paint = Paint()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = resources.getColor(R.color.darker_gray,null)
        paint.strokeWidth = lineHeight / 10f
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        val startX = paddingLeft.toFloat()
        val stopX = (width - paddingRight).toFloat()
        val offsetY: Float = paddingTop - getPaint().fontMetrics.top + paint.strokeWidth / 2
        for (i in 0 until lineCount) {
            val y = offsetY + lineHeight * i
            canvas.drawLine(startX, y, stopX, y, paint)
        }
        super.onDraw(canvas)
    }
}