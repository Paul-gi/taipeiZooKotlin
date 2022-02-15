package com.example.taipeizookotlin.Util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.taipeizookotlin.R


class ProgressDialogCustom(private var mContext: Context) : DialogFragment() {
    private var mDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.progressbar, container, false)
        initView(mContext)
        return view
    }

    private fun initView(pContext: Context) {
//        var lp = dialog!!.window!!.attributes
//        var window = mDialog?.window
        mDialog = Dialog(pContext)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
//        window!!.setLayout(lp.width, 200)
    }
}
