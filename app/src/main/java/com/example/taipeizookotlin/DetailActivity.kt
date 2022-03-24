package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taipeizookotlin.Adapter.GoogleMapGeoAdapter
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.Firebase.FirebaseService
import com.example.taipeizookotlin.Fragment.BaseFragment.Companion.mPageStackCount
import com.example.taipeizookotlin.Room.AppDataBase
import com.example.taipeizookotlin.Room.User
import com.example.taipeizookotlin.Service.RetrofitManager
import com.example.taipeizookotlin.Service.ZooApiService
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.Util.UtilCommonStr.Companion.getInstance
import com.example.taipeizookotlin.Util.UtilTools
import com.example.taipeizookotlin.databinding.MainDetailActivityBinding
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var mTitle = ""
    private var mListData: ListData = ListData()
    private var mLocationPositionData: LocationPositionData = LocationPositionData()
    private var mGoogleMapGeoAdapter: GoogleMapGeoAdapter = GoogleMapGeoAdapter()
    private var mLocationPositionListData: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()
    private var mGeoListData: ArrayList<LocationPositionData> = ArrayList()
    private var mUtilTools: UtilTools = UtilTools()
    private lateinit var mDataBinding: MainDetailActivityBinding
    private var mFirebaseHavePageCode = false
    private var mProgress: AlertDialog? = null
    private var mFirebasePageCode = -1
    private var mFromFirebase = false

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.main_detail_activity)
        mProgress = AlertDialog.Builder(this).create()
        val inflater: LayoutInflater = this.layoutInflater
        mProgress?.setView(inflater.inflate(R.layout.progressbar, null))
        mProgress?.show()

        getBundleData()

        if (mFromFirebase) {
            goToDetailPage(mTitle, mFirebasePageCode)
        } else {
            initView()
            mProgress?.cancel()
        }
    }


    private fun initView() {
        when (mTitle) {
            getInstance().mAnimal -> {
                initAnimalView()
            }
            getInstance().mPlant -> {
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mTitle = intent?.extras?.getString("TransformNotificationFirebasePageTitle") ?: ""
        mFirebasePageCode =
            intent?.extras?.getInt("TransformNotificationFirebasePageCode") ?: -1
        val iIntent = Intent()
        val iBundle = Bundle()
        iIntent.setClass(this, DetailActivity::class.java)
        iBundle.putBoolean("TransformNotificationFromFirebase", true)
        iBundle.putString("TransformNotificationFirebasePageTitle", mTitle)
        iBundle.putInt("TransformNotificationFirebasePageCode", mFirebasePageCode)
        iIntent.putExtras(iBundle)
        startActivity(iIntent)
    }

    private fun getBundleData() {
        mFromFirebase = intent?.extras?.getBoolean("TransformNotificationFromFirebase") ?: false
        if (mFromFirebase) {
            mTitle = intent.extras?.getString("TransformNotificationFirebasePageTitle") ?: ""
            mFirebasePageCode =
                intent?.extras?.getInt("TransformNotificationFirebasePageCode") ?: -1
            mPageStackCount += 1
        } else {
            mTitle = intent.extras?.getString("TitleName") ?: ""
            mTitle.let {
                mListData.setRawJson(it, intent.extras?.getString("ListDataAdapterListData"))
            }
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
        Log.d("qqq", mListData.getRawData())
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
        Log.d("qqq", "333")
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


    private fun goToDetailPage(
        mFirebasePageTitle: String, mFirebasePageCode: Int,
    ) {
        mCallApi(mFirebasePageTitle, mFirebasePageCode)
    }

    private fun mCallApi(pTitle: String, pPageCode: Int) {
        val mCallDetail: Call<JsonObject>?
        val mZooApiService: ZooApiService =
            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)
        val mUtilCommonStr: UtilCommonStr = getInstance()
        var iApiSelectTitle = ""


        when (pTitle) {
            "Animal" -> iApiSelectTitle = mUtilCommonStr.mAnimal
            "Plant" -> iApiSelectTitle = mUtilCommonStr.mPlant
            "OutSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mOutSideArea
                FirebaseService.mFcmFromInDepartmentBackPage = true
            }
            "InSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mInSideArea
                FirebaseService.mFcmFromInDepartmentBackPage = true
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

                    mTitle = iApiSelectTitle
                    mListData.setRawJson(mTitle!!, iListData[0].getRawData())

                    initView()
                    mFirebaseHavePageCode = false
                    mProgress?.cancel()

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
}