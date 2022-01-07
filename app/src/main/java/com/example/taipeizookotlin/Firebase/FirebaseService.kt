package com.example.taipeizookotlin.Firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.taipeizookotlin.MainActivity
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {

    private val mChannelID = "notificationChannelID"
    private val mChannelName = "com.example.taipeizookotlin"

    /**
     * 如果沒有firebasePageCode參數就是-1
     * -1的話走正常的開啟流程
     */
    companion object {
        var mFirebasePageTitle = ""
        var mFirebasePageCode: Int? = null
        var mFcmFromInDepartmentBackPage = false
    }


    override fun onNewToken(mToken: String) {
        super.onNewToken(mToken)
        Log.d(mToken, "getToken")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(pRemoteMessage: RemoteMessage) {
        super.onMessageReceived(pRemoteMessage)
        mFirebasePageTitle = pRemoteMessage.data["Title"].toString()
        mFirebasePageCode = pRemoteMessage.data["PageCode"]!!.toInt()

        //如果是室內區或室外區就打開 用於切換頁的動作
        if (mFirebasePageTitle == "OutSideArea" || mFirebasePageTitle == "InSideArea") {
            mFcmFromInDepartmentBackPage = true
        }

        wakeUpPhone()

        sendNotification(mFirebasePageTitle, mFirebasePageCode)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(pTitle: String, pMessage: Int?) {
        val iIntent = Intent(this, MainActivity::class.java)


        iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                iIntent, PendingIntent.FLAG_ONE_SHOT
            )


        /**
         * 兩種客製化版本
         */
//        var iNotification = NotificationCompat.Builder(this, mChannelID)
//            .setSmallIcon(R.drawable.logo)
////            .setContentTitle(pTitle)
////            .setContentText(pMessage.toString())
//            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.button_radius))
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomContentView(getRemoteView(pTitle, pMessage?.toString()))
//            .setCustomBigContentView(getRemoteView(pTitle, pMessage?.toString()))
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)

        val iNotification = NotificationCompat.Builder(applicationContext, mChannelID)
            //.setWhen(System.currentTimeMillis())
            .setContent(getRemoteView(pTitle, pMessage?.toString()))
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(pTitle)
            .setContentText(pMessage.toString())
            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.button_radius))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val iNotificationChannel =
                NotificationChannel(mChannelID, mChannelName, NotificationManager.IMPORTANCE_HIGH)

            mNotificationManager.createNotificationChannel(iNotificationChannel)
        }


        mNotificationManager.notify(Random.nextInt(5), iNotification.build())


    }


    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(pTitle: String, pMessage: String?): RemoteViews {
        val iRemoteView = RemoteViews(packageName, R.layout.fcm_remoteview)
        iRemoteView.setTextViewText(R.id.mTitle, pTitle)
        iRemoteView.setTextViewText(R.id.mMessage, pMessage)
        iRemoteView.setImageViewResource(R.id.mSmallIcon, R.drawable.logo)

        return iRemoteView

    }


    @SuppressLint("InvalidWakeLockTag")
    private fun wakeUpPhone() {
        val iPowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = iPowerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //開啟螢幕(10min)
        wl.acquire(10 * 60 * 1000L)
        //釋放資源
        wl.release()
    }
}