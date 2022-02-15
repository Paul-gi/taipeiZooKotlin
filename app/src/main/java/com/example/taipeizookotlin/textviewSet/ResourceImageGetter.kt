package com.example.taipeizookotlin.textviewSet

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import androidx.core.content.ContextCompat

class ResourceImageGetter(private val mContent: Context) : Html.ImageGetter {

    // Constructor takes a Context
    override fun getDrawable(source: String): Drawable? {
        Log.v("aaa","source=$source")
        val path: Int = mContent.resources.getIdentifier(
            source, "drawable", mContent.packageName
        )
        val drawable = ContextCompat.getDrawable(mContent, path)
        drawable?.setBounds(
            0, 0,
            drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )
        return drawable
    }
}