package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.Firebase.FirebaseService
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFcmFromInDepartmentBackPage
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageCode
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageTitle
import com.example.taipeizookotlin.Firebase.TransformNotification
import com.example.taipeizookotlin.Fragment.HomeFragment
import com.example.taipeizookotlin.Fragment.ListPageFragment
import com.example.taipeizookotlin.Service.RetrofitManager
import com.example.taipeizookotlin.Service.ZooApiService
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null


    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("aaa", "MainActivity=${FirebaseService.mFirebaseHavePageCode}")
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        /**
         * mFirebaseHavePageCode = true 代表有PageCode跟TitleName 所以走詳細頁面
         * 如果沒有PageCode但是有TitleName 就走列表頁
         * 否則走一般的流程
         */
        if (FirebaseService.mFirebaseHavePageCode) {
            goToDetailPage("Animal", 5)
        } else if (mFirebasePageTitle != "" && mFirebasePageCode == -1) {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, ListPageFragment(), ListPageFragment::class.java.simpleName)
                .commit()
        } else {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }
        this.supportFragmentManager.beginTransaction()
            .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
        fcmVirtualData()
    }


    private fun goToDetailPage(
        mFirebasePageTitle: String, mFirebasePageCode: Int,
    ) {
        Thread { mCallApi(mFirebasePageTitle, mFirebasePageCode) }.start()
    }

    private fun mCallApi(pTitle: String, pPageCode: Int) {
        val mCallDetail: Call<JsonObject>?
        val mZooApiService: ZooApiService =
            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)
        val mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
        var iApiSelectTitle = ""


        when (pTitle) {
            "Animal" -> iApiSelectTitle = mUtilCommonStr.mAnimal
            "Plant" -> iApiSelectTitle = mUtilCommonStr.mPlant
            "OutSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mOutSideArea
                mFcmFromInDepartmentBackPage = true
            }
            "InSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mInSideArea
                mFcmFromInDepartmentBackPage = true
            }
        }

        mCallDetail = when (pTitle) {
            "Animal" -> {
                mZooApiService.getAnimalData(1, pPageCode)
            }
            "Plant" -> {
                mZooApiService.getPlantData(1, pPageCode)
            }
            else -> {
                mZooApiService.getPavilionData(iApiSelectTitle, 1, pPageCode)
            }
        }



        mCallDetail.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {

                    val iListData: ArrayList<ListData> = ArrayList()
                    assert(response.body() != null)
                    val ix = JSONObject(response.body().toString())
                    val iz = ix.getJSONObject("result").getJSONArray("results")
                    val iData = ListData()
                    iData.setData(iz.getJSONObject(0))
                    iData.selectType(iApiSelectTitle, false)
                    iListData.add(iData)

                    val iIntent = Intent()
                    val iBundle = Bundle()
                    iBundle.putString("TitleName", iApiSelectTitle)
                    iBundle.putString("ListData", iListData[0].getRawData())
                    iIntent.putExtras(iBundle)
                    iIntent.setClass(this@MainActivity, DetailActivity::class.java)
                    startActivity(iIntent)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    if (e.toString() == "org.json.JSONException: Index 0 out of range [0..0)") {
                        Log.d("FirebaseFcmErrorMessage", e.toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("callApiError", t.toString())
            }
        })
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.v("aaa","onNewIntent")
    }


    /**
     * 推播假資料
     */
    private val mChannelID = "notificationChannelID"
    private val mChannelName = "com.example.taipeizookotlin"

    private var mIntent = Intent()
    private fun fcmVirtualData() {
        mFirebasePageCode = 5
        mFirebasePageTitle = "Animal"
        FirebaseService.mFirebaseHavePageCode = true


        mIntent = Intent(this, TransformNotification::class.java)
        sendNotification("Animal", 5)
    }

    private fun sendNotification(pTitle: String, pMessage: Int) {
//        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
        mNotificationManager.notify(System.currentTimeMillis().toInt(), iNotification.build())
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