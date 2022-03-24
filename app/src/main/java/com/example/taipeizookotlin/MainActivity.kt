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
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
    private var mIndex = 0

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        getBundleData(intent)
        selectPage()
        fcmTest()
    }

    private fun selectPage() {

        if (mFirebasePageTitle != "" && mFirebasePageCode == -1) {
            val iBundle = Bundle()
            iBundle.putString("MainFirebasePageTitle", mFirebasePageTitle)
            iBundle.putBoolean("MainFromFireBase", mFromFireBase)

            val iIndex = mIndex - 1
            val iListPageFragTag = ListPageFragment::class.java.simpleName + ",mIndex=$iIndex"
            var iFragmentTag: Fragment? = null
            for (iTag in supportFragmentManager.fragments) {
                if (iTag.tag != null && iTag.tag.toString() == iListPageFragTag && !iTag.isHidden) { // list
                    iFragmentTag = iTag
                    break
                }
                if (iTag.tag != null && iTag.tag.toString() == HomeFragment::class.java.simpleName && !iTag.isHidden) {// home
                    iFragmentTag = iTag
                    break
                }
            }

            val iListPageFragment = iFragmentTag
            if (iListPageFragment != null) {

              //  mFragmentSet(ListPageFragment(),iBundle,ListPageFragment::class.java.simpleName + ",mIndex=$mIndex",iListPageFragment)
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.mFragment,
                        ListPageFragment().javaClass,
                        iBundle,
                        ListPageFragment::class.java.simpleName + ",mIndex=$mIndex"
                    )
                    .hide(iListPageFragment)
                    .addToBackStack(null)
                    .commit()

            } else {
                this.supportFragmentManager.beginTransaction()
                    .add(
                        R.id.mFragment,
                        ListPageFragment().javaClass,
                        iBundle,
                        ListPageFragment::class.java.simpleName + ",mIndex=$mIndex"
                    )
                    .commit()
            }
            mIndex++
        } else {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }
    }


    private fun mFragmentSet(
        pAddFragment: Fragment,
        @Nullable pBundle: Bundle,
        @Nullable pFragmentTag: String,
        @Nullable pHideFragment: Fragment
    ) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.mFragment,
                pAddFragment.javaClass,
                pBundle,
                pFragmentTag
            )
            .hide(pHideFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getBundleData(intent)
        selectPage()
    }

    private fun getBundleData(pIntent: Intent?) {
        mFirebasePageTitle =
            pIntent?.extras?.getString("TransformNotificationFirebasePageTitle") ?: ""
        mFirebasePageCode = pIntent?.extras?.getInt("TransformNotificationFirebasePageCode") ?: -1
        mFromFireBase = pIntent?.extras?.getBoolean("TransformNotificationFromFirebase") ?: false
    }


    private fun fcmTest() {
        checkNotification("Animal", -1)
//        checkNotification("Plant", -1)
//        checkNotification("OutSideArea", -1)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun checkNotification(pTitle: String, pPageCode: Int) {
        val mBundle = Bundle()
        mIntent = Intent(this, TransformNotification::class.java)
        mBundle.putString("FirebasePageTitle", pTitle)
        mBundle.putInt("FirebasePageCode", pPageCode)
        mIntent.putExtras(mBundle)
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