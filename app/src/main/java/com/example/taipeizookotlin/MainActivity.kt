package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.Firebase.TransformNotification
import com.example.taipeizookotlin.Fragment.HomeFragment
import com.example.taipeizookotlin.Fragment.ListPageFragment
import com.example.taipeizookotlin.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private var mIntent = Intent()
    private var mFirebasePageTitle = ""
    private var mFirebasePageCode = -1
    private var mFromFireBase = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        getBundleData()
        selectPage()
        // fcmTest()


    }

    private fun selectPage() {
        if (mFirebasePageTitle != "" && mFirebasePageCode == -1) {
            val iBundle = Bundle()
            iBundle.putString("MainFirebasePageTitle", mFirebasePageTitle)
            iBundle.putBoolean("MainFromFireBase", mFromFireBase)
            ListPageFragment().arguments = iBundle
            this.supportFragmentManager.beginTransaction()
                .add(
                    R.id.mFragment,
                    ListPageFragment().javaClass,
                    iBundle,
                    ListPageFragment::class.java.simpleName
                )
                .commit()
        } else {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getBundleData()
        selectPage()
    }

    private fun getBundleData() {
        mFirebasePageTitle =
            intent?.extras?.getString("TransformNotificationFirebasePageTitle") ?: ""
        mFirebasePageCode = intent?.extras?.getInt("TransformNotificationFirebasePageCode") ?: -1
        mFromFireBase = intent?.extras?.getBoolean("TransformNotificationFromFirebase") ?: false
    }


//    private fun fcmTest() {
//        FirebaseService.mFirebasePageTitle = "Plant"
//        FirebaseService.mFirebasePageCode = 3
//        FirebaseService.mFirebaseHavePageCode = true
//        checkNotification(
//            FirebaseService.mFirebasePageTitle,
//            FirebaseService.mFirebasePageCode
//        )
//    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun checkNotification(pTitle: String, pPageCode: Int) {
        mIntent = Intent(this, TransformNotification::class.java)
        sendNotification(pTitle, pPageCode)
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    private fun sendNotification(pTitle: String, pMessage: Int) {
        val mChannelID = "notificationChannelID"
        val mChannelName = "com.example.taipeizookotlin"

        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        /**
         * PendingIntent.getActivity 正常activity流程
         * PendingIntent.getBroadcast走廣播改造流程
         */
//        val pendingIntent =
//            PendingIntent.getActivity(
//                this,
//                0,
//                mIntent, PendingIntent.FLAG_UPDATE_CURRENT
//            )
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                mIntent, PendingIntent.FLAG_UPDATE_CURRENT
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
            .setContent(getRemoteView(pTitle, pMessage.toString()))
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

}