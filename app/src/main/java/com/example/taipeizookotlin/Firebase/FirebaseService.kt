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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.taipeizookotlin.MainActivity
import com.example.taipeizookotlin.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {

    private val mChannelID = "notificationChannelID"
    private val mChannelName = "com.example.taipeizookotlin"
    private var mIntent = Intent()

    /**
     * 如果沒有firebasePageCode參數就是-1
     * -1的話走正常的開啟流程
     * -2就是走到Detail詳細頁面內
     */
    companion object {
        var mFirebasePageTitle = ""
        var mFirebasePageCode = -1
        var mFcmFromInDepartmentBackPage = false
        var mFirebaseHavePageCode = false
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


        // 如果有FCM_PageCode就會開啟 打API的時候做判斷 如果有PageCode代表去詳細頁面
        if (mFirebasePageCode != -1 && mFirebasePageTitle != "") {
            mFirebaseHavePageCode = true
        }

        wakeUpPhone()

        checkNotification(mFirebasePageTitle, mFirebasePageCode)
    }


    /**
     * mFirebaseHavePageCode = true 代表有PageCode跟TitleName 所以走詳細頁面
     * 如果沒有PageCode但是有TitleName 就走列表頁
     * 否則走一般的流程
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun checkNotification(pTitle: String, pPageCode: Int) {
        //LocalBroadcastManager.getInstance(applicationContext).registerReceiver(mLocalBroadcastReceiver, getLocalIntentFilter())
        mIntent = Intent(this, TransformNotification::class.java)
        sendNotification(pTitle, pPageCode)

    }

    @SuppressLint("UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    private fun sendNotification(pTitle: String, pMessage: Int) {
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


//    private fun goToDetailPage(
//        mFirebasePageTitle: String, mFirebasePageCode: Int,
//    ) {
//        Thread { mCallApi(mFirebasePageTitle, mFirebasePageCode) }.start()
//    }
//
//    private fun mCallApi(pTitle: String, pPageCode: Int) {
//        val mCallDetail: Call<JsonObject>?
//        val mZooApiService: ZooApiService =
//            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)
//        val mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
//        var iApiSelectTitle = ""
//
//
//        when (pTitle) {
//            "Animal" -> iApiSelectTitle = mUtilCommonStr.mAnimal
//            "Plant" -> iApiSelectTitle = mUtilCommonStr.mPlant
//            "OutSideArea" -> {
//                iApiSelectTitle = mUtilCommonStr.mOutSideArea
//                mFcmFromInDepartmentBackPage = true
//            }
//            "InSideArea" -> {
//                iApiSelectTitle = mUtilCommonStr.mInSideArea
//                mFcmFromInDepartmentBackPage = true
//            }
//        }
//
//        mCallDetail = when (pTitle) {
//            "Animal" -> {
//                mZooApiService.getAnimalData(1, pPageCode)
//            }
//            "Plant" -> {
//                mZooApiService.getPlantData(1, pPageCode)
//            }
//            else -> {
//                mZooApiService.getPavilionData(iApiSelectTitle, 1, pPageCode)
//            }
//        }
//
//
//        mIntent = Intent(this, MainActivity::class.java)
//        mCallDetail.enqueue(object : Callback<JsonObject?> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                try {
//
//                    val iListData: ArrayList<ListData> = ArrayList<ListData>()
//                    assert(response.body() != null)
//                    val ix = JSONObject(response.body().toString())
//                    val iz = ix.getJSONObject("result").getJSONArray("results")
//                    val iData = ListData()
//                    iData.setData(iz.getJSONObject(0))
//                    iData.selectType(iApiSelectTitle, false)
//                    iListData.add(iData)
//
//
//                    mBundle.putString("TitleName", iApiSelectTitle)
//                    mBundle.putString("ListData", iListData[0].getRawData())
//                    mIntent.putExtras(mBundle)

    /**
     * 直接從推播去詳細 不透過main 記得
     * sendNotification(pTitle, pPageCode)
     */
//                    sendNotification(pTitle, pPageCode)
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    if (e.toString() == "org.json.JSONException: Index 0 out of range [0..0)"){
//                        Log.d("FirebaseFcmErrorMessage", e.toString())
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Log.d("callApiError", t.toString())
//            }
//        })
//    }
}