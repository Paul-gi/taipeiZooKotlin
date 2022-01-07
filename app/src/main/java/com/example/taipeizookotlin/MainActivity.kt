package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageTitle
import com.example.taipeizookotlin.Fragment.HomeFragment
import com.example.taipeizookotlin.Fragment.ListPageFragment
import com.example.taipeizookotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    var mFormFcmPageCode = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //如果是走過FCM的詳細頁面進去的 會在這裡判斷狀態 之後選擇要回去列表頁 還是正常走首頁
        val iBundle = intent.extras
        if (iBundle != null) {
            mFirebasePageTitle = iBundle.getString("mFirebasePageTitle").toString()
            mFormFcmPageCode = iBundle.getBoolean("isFromFCMPageCode")
        }



        if (mFirebasePageTitle != "" || mFormFcmPageCode) {
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