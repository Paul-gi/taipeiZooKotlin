package com.example.taipeizookotlin.Firebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.taipeizookotlin.DetailActivity
import com.example.taipeizookotlin.MainActivity

class TransformNotification : BroadcastReceiver() {

    private var mIntent = Intent()
    private var mBundle = Bundle()


    /**
     * 未使用 可以從這裡接收到推播傳送過來這裡抓到資料 以後可以使用
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val iFirebasePageTitle = intent?.extras?.getString("FirebasePageTitle") ?: ""
        val iFirebasePageCode = intent?.extras?.getInt("FirebasePageCode") ?: -1

        if (iFirebasePageCode != -1 && iFirebasePageTitle != "") {
            mIntent = Intent(context, DetailActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else {
            mIntent = Intent(context, MainActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        mBundle.putString("TransformNotificationFirebasePageTitle", iFirebasePageTitle)
        mBundle.putInt("TransformNotificationFirebasePageCode", iFirebasePageCode)
        mBundle.putBoolean("TransformNotificationFromFirebase", true)
        mIntent.putExtras(mBundle)
        context?.startActivity(mIntent)
    }
}