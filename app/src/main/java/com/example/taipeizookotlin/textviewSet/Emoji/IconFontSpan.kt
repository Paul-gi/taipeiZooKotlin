package com.example.taipeizookotlin.textviewSet.Emoji

import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint

import android.text.style.MetricAffectingSpan


 class IconFontSpan(context: Context) : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.typeface = typeface
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.typeface = typeface
    }

    companion object {
        private var typeface: Typeface? = null
    }

    init {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(
                context.assets, "icomoon.ttf"
            )
        }
    }
}