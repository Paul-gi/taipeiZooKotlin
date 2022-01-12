package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.Firebase.FirebaseService
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageCode
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageTitle
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
import java.util.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (mFirebasePageTitle != "" && mFirebasePageCode == -1) {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, ListPageFragment(), ListPageFragment::class.java.simpleName)
                .commit()
        } else {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }

    }

}