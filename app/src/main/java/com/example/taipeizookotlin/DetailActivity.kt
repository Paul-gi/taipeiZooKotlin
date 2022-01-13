package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taipeizookotlin.Adapter.GoogleMapGeoAdapter
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebaseHavePageCode
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFirebasePageCode
import com.example.taipeizookotlin.Room.AppDataBase
import com.example.taipeizookotlin.Room.User
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.Util.UtilTools
import com.example.taipeizookotlin.databinding.MainDetailActivityBinding
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var mTitle: String? = null
    private var mListData: ListData = ListData()
    private var mLocationPositionData: LocationPositionData = LocationPositionData()
    private var mGoogleMapGeoAdapter: GoogleMapGeoAdapter = GoogleMapGeoAdapter()
    private var mLocationPositionListData: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()
    private var mGeoListData: ArrayList<LocationPositionData> = ArrayList()
    private var mUtilTools: UtilTools = UtilTools()
    private lateinit var mDataBinding: MainDetailActivityBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.main_detail_activity)
        Log.v("aaa", "DetailActivity")
        return
        getBundleData()
        when (mTitle) {
            UtilCommonStr.getInstance().mAnimal -> {
                initAnimalView()
            }
            UtilCommonStr.getInstance().mPlant -> {
                initPlantView()
            }
            else -> {
                initDepartment()
            }
        }
        mDataBinding.mTitleBar.mToolbar.title = mTitle
        mDataBinding.mTitleBar.mChange.visibility = View.GONE
        initBelowView()
    }

    private fun getBundleData() {
//        val mBundle: Bundle? = intent.extras
//        if (mBundle != null) {
//            mTitle?.let {
//                mTitle = mBundle.getString("TitleName")
//                mListData.setRawJson(it, mBundle.getString("ListData"))
//            }
//        }
        mTitle = intent.extras?.getString("TitleName")
        mTitle?.let {
            mListData.setRawJson(it, intent.extras?.getString("ListData"))
        }
    }

    private fun setRoom() {
        Thread {
            val mApDataBase: AppDataBase? = this.let { AppDataBase.getInstance(it) }
            mApDataBase?.userDao()?.insertUser(
                User().apply {
                    pageName = mTitle
                    clickPosition = mFirebasePageCode
                    EnglishName = mListData.getEnglishName()
                    ChineseName = mListData.getChineseName()
                }
            )
            if (mFirebaseHavePageCode) {
                mFirebasePageCode = -2
            }
        }.start()
    }


    private fun initDepartment() {
        mDataBinding.mDepartmentDetail.root.visibility = View.VISIBLE
        mDataBinding.mAnimalDetail.root.visibility = View.GONE
        mDataBinding.mPlantDetail.root.visibility = View.GONE
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.visibility = View.GONE
        mUtilTools.setData(mListData.getKeyEName(), mDataBinding.mDepartmentDetail.mEName)
        mUtilTools.setData(mListData.getKeyKeyInfo(), mDataBinding.mDepartmentDetail.mEInfo)
        mUtilTools.setData(mListData.getKeyMemo(), mDataBinding.mDepartmentDetail.mEMemo)
        mUtilTools.setData(
            mListData.getKeyDistribution(),
            mDataBinding.mDepartmentDetail.mADistribution
        )
        mDataBinding.mDepartmentDetail.mDistributionView.visibility = View.GONE
        mDataBinding.mDepartmentDetail.mADistribution.visibility = View.GONE
    }

    private fun initAnimalView() {
        mDataBinding.mDepartmentDetail.root.visibility = View.GONE
        mDataBinding.mAnimalDetail.root.visibility = View.VISIBLE
        mDataBinding.mPlantDetail.root.visibility = View.GONE
        mUtilTools.setData(mListData.getChineseName(), mDataBinding.mAnimalDetail.mANameCh)
        mUtilTools.setData(mListData.getEnglishName(), mDataBinding.mAnimalDetail.mANameEn)
        mUtilTools.setData(mListData.getKeyBehavior(), mDataBinding.mAnimalDetail.mABehavior)
        mUtilTools.setData(
            mListData.getKeyDistribution(),
            mDataBinding.mAnimalDetail.mADistribution
        )
        mUtilTools.setData(mListData.getKeyClass(), mDataBinding.mAnimalDetail.mAClass)
        mUtilTools.setData(mListData.getKeyFamily(), mDataBinding.mAnimalDetail.mAFamily)
        mUtilTools.setTextScrollView(mDataBinding.mAnimalDetail.mABehavior)
    }

    private fun initPlantView() {
        mDataBinding.mDepartmentDetail.root.visibility = View.GONE
        mDataBinding.mAnimalDetail.root.visibility = View.GONE
        mDataBinding.mPlantDetail.root.visibility = View.VISIBLE
        mUtilTools.setData(mListData.getChineseName(), mDataBinding.mPlantDetail.mANameCh)
        mUtilTools.setData(mListData.getEnglishName(), mDataBinding.mPlantDetail.mANameEn)
        mUtilTools.setData(mListData.getKeyAlsoKnown(), mDataBinding.mPlantDetail.mAAlsoKnown)
        mUtilTools.setData(mListData.getKeyBrief(), mDataBinding.mPlantDetail.ABrief)
        mUtilTools.setData(mListData.getKeyGenus(), mDataBinding.mPlantDetail.mAGenus)
        mUtilTools.setData(mListData.getKeyFeature(), mDataBinding.mPlantDetail.mAFeature)
        mUtilTools.setData(mListData.getKeyFamily(), mDataBinding.mPlantDetail.mAFamily)
        mUtilTools.setData(
            mListData.getKeyFunctionApplication(),
            mDataBinding.mPlantDetail.mAFunctionApplication
        )
    }

    private fun initBelowView() {
        try {
            mUtilTools.setPictureGone(
                this,
                mListData.keyUrl01()!!,
                mDataBinding.mBelowDetail.mAPic01URL,
                mDataBinding.mBelowDetail.mAPic01ALT,
                mDataBinding.mBelowDetail.mImageTitle
            )
            mUtilTools.setPictureGone(
                this,
                mListData.keyUrl02()!!,
                mDataBinding.mBelowDetail.mAPic02URL,
                mDataBinding.mBelowDetail.mAPic02ALT
            )
            mUtilTools.setPictureGone(
                this,
                mListData.keyUrl03()!!,
                mDataBinding.mBelowDetail.mAPic03URL,
                mDataBinding.mBelowDetail.mAPic03ALT
            )
            mUtilTools.setPictureGone(
                this,
                mListData.keyUrl04()!!,
                mDataBinding.mBelowDetail.mAPic04URL,
                mDataBinding.mBelowDetail.mAPic04ALT
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mUtilTools.setData(mListData.getKeyAlt01(), mDataBinding.mBelowDetail.mAPic01ALT)
        mUtilTools.setData(mListData.getKeyAlt03(), mDataBinding.mBelowDetail.mAPic03ALT)
        mUtilTools.setData(mListData.getKeyAlt02(), mDataBinding.mBelowDetail.mAPic02ALT)
        mUtilTools.setData(mListData.getKeyAlt04(), mDataBinding.mBelowDetail.mAPic04ALT)
        mUtilTools.setGeo(mListData, mLocationPositionData, mGeoListData)
        mUtilTools.setLocation(mListData, mLocationPositionData, mLocationPositionListData)
        mDataBinding.mBelowDetail.mLocation.setOnClickListener {
            val iIntent = Intent()
            val iBundle = Bundle()
            iBundle.putSerializable("mLocationPositionListData", mLocationPositionListData)
            iIntent.setClass(this, GoogleMapActivity::class.java)
            iIntent.putExtras(iBundle)
            startActivity(iIntent)
        }
        mGoogleMapGeoAdapter.setData(mGeoListData)
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.layoutManager = LinearLayoutManager(this)
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.adapter = mGoogleMapGeoAdapter
        if (mListData.getKeyVedio().equals("")) {
            mDataBinding.mBelowDetail.mAVedioURL.visibility = View.GONE
            mDataBinding.mBelowDetail.mVdoView.visibility = View.GONE
        } else {
            //jumpVedioUrl
            mDataBinding.mBelowDetail.mAVedioURL.setOnClickListener {
                //點擊外開影片連結
//                Intent pIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((mListData.getKeyVedio())));
//                startActivity(pIntent);
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString("getUrl", mListData.getKeyVedio())
                intent.setClass(this@DetailActivity, WebViewActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        mDataBinding.mTitleBar.mBackBtn.setOnClickListener {
            setRoom()
            onBackPressed()
            this.finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setRoom()
        this.finish()
    }
}