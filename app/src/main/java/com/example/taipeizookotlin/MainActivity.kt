package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageCode
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageTitle
import com.example.taipeizookotlin.Fragment.HomeFragment
import com.example.taipeizookotlin.Fragment.ListPageFragment
import com.example.taipeizookotlin.Viewmodel.CallViewModel
import com.example.taipeizookotlin.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    var mFormFcmPageCode = false

    private val mCallViewModel: CallViewModel by lazy {
        ViewModelProvider(this).get(CallViewModel::class.java)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //如果是走過FCM的詳細頁面進去的 會在這裡判斷狀態 之後選擇要回去列表頁 還是正常走首頁
//        val iBundle = intent.extras
//        if (iBundle != null) {
//            mFirebasePageTitle = iBundle.getString("mFirebasePageTitle").toString()
//            mFormFcmPageCode = iBundle.getBoolean("isFromFCMPageCode")
//        }


        if (mFirebasePageTitle != "" && (mFirebasePageCode != -1 || mFirebasePageCode != null)) {
            getDetailPageData()



        } else if (mFirebasePageTitle != "") {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, ListPageFragment(), ListPageFragment::class.java.simpleName)
                .commit()
        } else {
            this.supportFragmentManager.beginTransaction()
                .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }

    }


    private fun getDetailPageData() {
        val mZooDataList: ArrayList<ListData> = ArrayList<ListData>()
        val iData: ListData = mZooDataList[mFirebasePageCode!!]
        Thread { mCallViewModel.mCallApi(mFirebasePageTitle) }.start()
        mCallViewModel.getDataListObserver().observe(this) { pCallData ->
            if (pCallData != null) {

            }
        }
        val iIntent = Intent()
        val iBundle = Bundle()
        iBundle.putString("TitleName", mTitleName)
        iBundle.putString("ListData", iData.getRawData())
        iIntent.putExtras(iBundle)
        intent.setClass(this, DetailActivity::class.java)
        intent.putExtras(iBundle)
        this.finish()
        startActivity(intent)

    }
}