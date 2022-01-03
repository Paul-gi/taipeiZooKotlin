@file:Suppress("PackageName")

package com.example.taipeizookotlin.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.Service.RetrofitManager
import com.example.taipeizookotlin.Service.ZooApiService
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class CallViewModel: ViewModel() {
    private val mDataList: MutableLiveData<ArrayList<ListData>?> =
        MutableLiveData<ArrayList<ListData>?>()
    private val mFinish = MutableLiveData<Boolean>()
    private var mIndex = 0
    private var mIsNotFinish = false
    private var mGetData = false
    private var mCall: Call<JsonObject>? = null

    fun getDataListObserver(): MutableLiveData<ArrayList<ListData>?> {
        return mDataList
    }

    fun getDataFinishState(): MutableLiveData<Boolean> {
        return mFinish
    }

    fun mCallApi(pTitleName: String) {
        if (mIsNotFinish) {
            return
        }
        if (mGetData) {
            return
        }
        synchronized(this) { mGetData = true }
        val mZooApiService: ZooApiService =
            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)

        mCall = when (pTitleName) {
            UtilCommonStr.getInstance().mAnimal -> {
                mZooApiService.getAnimalData(50, mIndex)
            }
            UtilCommonStr.getInstance().mPlant -> {
                mZooApiService.getPlantData(50, mIndex)
            }
            else -> {
                mZooApiService.getPavilionData(pTitleName, 50, mIndex)
            }
        }
        mCall!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val iListData: ArrayList<ListData> = ArrayList<ListData>()
                    assert(response.body() != null)
                    val ix = JSONObject(response.body().toString())
                    val iz = ix.getJSONObject("result").getJSONArray("results")
                    for (i in 0 until iz.length()) {
                        val iData = ListData()
                        iData.setData(iz.getJSONObject(i))
                        iData.selectType(pTitleName, false)
                        iListData.add(iData)
                    }
                    mDataList.postValue(iListData)
                    if (iListData.size == 50) {
                        mIndex += 50
                    } else {
                        mIsNotFinish = true
                        mFinish.postValue(true)
                    }
                    mGetData = false
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                mDataList.postValue(null)
            }
        })
    }
}