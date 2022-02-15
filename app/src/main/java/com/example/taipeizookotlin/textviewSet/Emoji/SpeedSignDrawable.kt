package com.example.taipeizookotlin.textviewSet.Emoji

import android.annotation.SuppressLint
import android.graphics.*
import android.widget.TextView

import android.graphics.drawable.Drawable


 class SpeedSignDrawable(textView: TextView, number: String) :
    Drawable() {
    private val ascent: Float
    private val descent: Float
    private val textSize: Float
    private val strokeWidth: Float
    private val number: String
    private val paint: Paint
    override fun draw(canvas: Canvas) {
        val size = (-ascent).toInt()

        // Draw the circle
        paint.style = Paint.Style.FILL
        paint.color = Color.YELLOW
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Draw the ring
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        val ringWidth = (size / 10).toFloat()
        paint.strokeWidth = ringWidth
        canvas.drawCircle(size / 2f, size / 2f, size / 2 - ringWidth / 2, paint)

        // Draw the text
        val ratio = 0.4f
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = textSize * ratio
        paint.strokeWidth = strokeWidth
        paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        val x = size / 2f
        val y = (size / 2f - (descent + ascent) / 2 * ratio)
        canvas.drawText(number, x, y, paint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    @SuppressLint("WrongConstant")
    override fun getOpacity(): Int {
        return 0
    }

    init {
        ascent = textView.paint.ascent()
        descent = textView.paint.descent()
        textSize = textView.textSize
        strokeWidth = textView.paint.strokeWidth
        this.number = number
        val size = (-ascent).toInt()
        this.setBounds(0, 0, size, size)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }
}