package com.example.taipeizookotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TransformNotification: BroadcastReceiver() {


    /**
     * 未使用 可以從這裡接收到推播傳送過來這裡抓到資料 以後可以使用
     */
    override fun onReceive(context: Context?, intent: Intent?) {

        val iBundle = intent?.extras
        if( iBundle != null) {
            context?.let {
                val intent = Intent(it, DetailActivity::class.java)
//                intent.addFlags(Intent.Activit)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtras(iBundle)
                context.startActivity(intent)
            }
        }
    }

}