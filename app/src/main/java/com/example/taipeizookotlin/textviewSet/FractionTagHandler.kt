package com.example.taipeizookotlin.textviewSet

import android.text.Editable
import android.text.Html
import android.text.Spannable
import org.xml.sax.XMLReader


class FractionTagHandler : Html.TagHandler {
    override fun handleTag(
        opening: Boolean,
        tag: String?,
        output: Editable?,
        xmlReader: XMLReader?,
    ) {
//        if (!"afrc".equals(attr.tag, ignoreCase = true)) {
//            return
//        }

        val len: Int = output!!.length
        if (opening) {
            output.setSpan(FractionSpan(), len, len, Spannable.SPAN_MARK_MARK)
        } else {
            val obj: Any = getLast(output, FractionSpan::class.java)!!
            val where = output.getSpanStart(obj)
            output.removeSpan(obj)
            if (where != len) {
                output.setSpan(FractionSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    private fun getLast(text: Editable, kind: Class<FractionSpan>): Any? {
        val iObj = text.getSpans(0, text.length, kind)
        return if (iObj.isEmpty()) {
            null
        } else {
            for (i in iObj.indices.reversed()) {
                if (text.getSpanFlags(iObj[i]) == Spannable.SPAN_MARK_MARK) {
                    return iObj[i]
                }
            }
            null
        }
    }
}