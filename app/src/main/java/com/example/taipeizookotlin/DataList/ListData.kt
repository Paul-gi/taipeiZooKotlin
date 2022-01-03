package com.example.taipeizookotlin.DataList

import com.example.taipeizookotlin.Util.UtilCommonStr
import org.json.JSONException
import org.json.JSONObject

class ListData {

    private var mJsonObject: JSONObject? = null
    private var mZooDataDetail: ZooDataDetail? = null
    private var keyUrl01 = ""
    private var keyChineseName = ""
    private var keyEnglishName = ""

    fun setData(asJsonObject: JSONObject?) {
        mJsonObject = asJsonObject
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun setRawJson(pTitleName: String, pRawJson: String?) {
        try {
            mJsonObject = JSONObject(pRawJson)

            when (pTitleName) {
                UtilCommonStr.getInstance().mAnimal -> {
                    setTypeAnimal(true)
                }
                UtilCommonStr.getInstance().mPlant -> {
                    setTypePlant(true)
                }
                else -> {
                    setTypeDepartment(true)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun selectType(pTitleName: String, pDetail: Boolean) {
        when (pTitleName) {
            UtilCommonStr.getInstance().mAnimal -> {
                setTypeAnimal(pDetail)
            }
            UtilCommonStr.getInstance().mPlant -> {
                setTypePlant(pDetail)
            }
            else -> {
                setTypeDepartment(pDetail)
            }
        }
    }

    private fun setTypeAnimal(pDetail: Boolean) {
        keyUrl01 = "A_Pic01_URL"
        keyChineseName = "\uFEFFA_Name_Ch"
        keyEnglishName = "A_Name_En"
        if (pDetail) {
            mZooDataDetail = ZooDataDetail("A")
        }
    }

    private fun setTypePlant(pDetail: Boolean) {
        keyUrl01 = "F_Pic01_URL"
        keyEnglishName = "F_Name_En"
        keyChineseName = "\uFEFFF_Name_Ch"
        if (pDetail) {
            mZooDataDetail = ZooDataDetail("F")
        }
    }

    private fun setTypeDepartment(pDetail: Boolean) {
        keyUrl01 = "E_Pic_URL"
        keyChineseName = "E_Name"
        if (pDetail) {
            mZooDataDetail = ZooDataDetail("E")
        }
    }

    fun getRawData(): String {
        return mJsonObject.toString()
    }

    fun keyUrl01(): String? {
        return getData(keyUrl01)
    }

    fun getEnglishName(): String? {
        return getData(keyEnglishName)
    }

    fun getChineseName(): String? {
        return getData(keyChineseName)
    }


    fun keyUrl02(): String? {
        return getData(mZooDataDetail!!.keyUrl02)
    }

    fun keyUrl03(): String? {
        return getData(mZooDataDetail!!.keyUrl03)
    }

    fun keyUrl04(): String? {
        return getData(mZooDataDetail!!.keyUrl04)
    }

    fun getKeyAlt01(): String? {
        return getData(mZooDataDetail!!.keyAlt01)
    }

    fun getKeyAlt02(): String? {
        return getData(mZooDataDetail!!.keyAlt02)
    }

    fun getKeyAlt03(): String? {
        return getData(mZooDataDetail!!.keyAlt03)
    }

    fun getKeyAlt04(): String? {
        return getData(mZooDataDetail!!.keyAlt04)
    }

    fun getKeyClass(): String? {
        return getData(mZooDataDetail!!.keyClass)
    }

    fun getKeyDistribution(): String? {
        return getData(mZooDataDetail!!.keyDistribution)
    }

    fun getKeyFamily(): String? {
        return getData(mZooDataDetail!!.keyFamily)
    }

    fun getKeyGeo(): String? {
        return getData(mZooDataDetail!!.keyGeo)
    }

    fun getKeyLocation(): String? {
        return getData(mZooDataDetail!!.keyLocation)
    }

    fun getKeyVedio(): String? {
        return getData(mZooDataDetail!!.keyVedio)
    }

    fun getKeyFunctionApplication(): String? {
        return getData(mZooDataDetail!!.KeyFunctionApplication)
    }

    fun getKeyGenus(): String? {
        return getData(mZooDataDetail!!.KeyGenus)
    }

    fun getKeyFeature(): String? {
        return getData(mZooDataDetail!!.KeyFeature)
    }

    fun getKeyBrief(): String? {
        return getData(mZooDataDetail!!.KeyBrief)
    }

    fun getKeyAlsoKnown(): String? {
        return getData(mZooDataDetail!!.KeymAlsoKnown)
    }

    fun getKeyKeyInfo(): String? {
        return getData(mZooDataDetail!!.KeyInfo)
    }

//    fun getKeyE_Pic_URL(): String? {
//        return getData(mZooDataDetail!!.KeyE_Pic_URL)
//    }

    fun getKeyMemo(): String? {
        return getData(mZooDataDetail!!.KeyMemo)
    }

    fun getKeyUrl(): String? {
        return getData(mZooDataDetail!!.KeyUrl)
    }

    fun getKeyEName(): String? {
        return getData(mZooDataDetail!!.KeyEName)
    }

    fun getKeyBehavior(): String? {
        return getData(mZooDataDetail!!.keyBehavior)
    }

    private fun getData(pKey: String?): String? {
        var pKey = pKey
        if (pKey == null || pKey == "") {
            pKey = ""
        }
        return try {
            mJsonObject!!.getString(pKey)
        } catch (e: JSONException) {
            e.printStackTrace()
            ""
        }
    }
}

@SuppressWarnings("SpellCheckingInspection")
class ZooDataDetail(pType: String) {
    /**
     * @param pType + _Pic02_URL
     * 因管區 最前面為 “X+固定字串”
     */
    var keyUrl02 = pType + "_Pic02_URL"
    var keyUrl03 = pType + "_Pic03_URL"
    var keyUrl04 = pType + "_Pic04_URL"
    var keyAlt01 = pType + "_Pic01_ALT"
    var keyAlt02 = pType + "_Pic02_ALT"
    var keyAlt03 = pType + "_Pic03_ALT"
    var keyAlt04 = pType + "_Pic04_ALT"
    var keyBehavior = pType + "_Behavior"
    var keyDistribution = pType + "_Distribution"
    var keyClass = pType + "_Class"
    var keyFamily = pType + "_Family"
    var keyVedio = pType + "_Vedio_URL"
    var keyLocation = pType + "_Location"
    var keyGeo = pType + "_Geo"
    var KeymAlsoKnown = pType + "_AlsoKnown"
    var KeyBrief = pType + "_Brief"
    var KeyFeature = pType + "_Feature"
    var KeyGenus = pType + "_Genus"
    var KeyFunctionApplication = pType + "_Function＆Application"
    var KeyE_Pic_URL = pType + "_Pic_URL"
    var KeyInfo = pType + "_Info"
    var KeyMemo = pType + "_Memo"
    var KeyUrl = pType + "_URL"
    var KeyEName = pType + "_Name"
}