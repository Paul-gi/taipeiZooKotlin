package com.example.taipeizookotlin.textviewSet

import android.text.TextPaint

import android.text.style.MetricAffectingSpan


internal class FractionSpan : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.fontFeatureSettings = FONT_FEATURE_SETTINGS
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.fontFeatureSettings = FONT_FEATURE_SETTINGS
    }

    companion object {
        private const val FONT_FEATURE_SETTINGS = "afrc"
    }
}