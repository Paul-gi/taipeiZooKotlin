package com.example.taipeizookotlin.Util
import android.annotation.SuppressLint
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Service.RetrofitManager
import java.io.InputStream
import java.util.*

class UtilTools {
    private var isFirst = true

    /**
     * @param pString 圖片url位址
     * @param pImage  image位址
     * 因為https憑證問題所以使用isFirst 如果call過一次之後就不用再加載了
     * gilde以後記得沒圖片要看minifast權限＆xml的netWork&這裡要抓憑證
     */
    @Throws(Exception::class)
    fun controlPicture(pContext: Context?, pString: String, pImage: ImageView?) {
        val iStringSplit: String = pString.replace("\\?".toRegex(), "")
        if (isFirst) {
            Glide.get(pContext!!).registry
                .replace(
                    GlideUrl::class.java, InputStream::class.java,
                    OkHttpUrlLoader.Factory(RetrofitManager().getInstance()?.getSSLOkHttpClient()!!)
                )
            isFirst = false
        }
        //https@ //www.zoo.gov.tw/iTAP/03_Animals/InsectHouse/0_InsectHouse/TS/Trachychorax Sexpunctatus01.jpg
        pImage?.let {
            Glide.with(pContext!!)
                .asBitmap()
                .load(iStringSplit)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(it)
        }
    }

    /**
     * @param pContext        context
     * @param pURL            取得URL 判斷有無資料
     * @param pImageView      圖片欄位
     * @param pTextView       圖片名稱欄位
     * @param mImageTitleView 第一張圖片都沒有則把Title也mark掉
     */
    fun setPictureGone(
        pContext: Context?,
        pURL: String,
        pImageView: ImageView,
        pTextView: TextView?,
        mImageTitleView: TextView
    ) {
        if (pURL == "") {
            pImageView.visibility = View.GONE
            mImageTitleView.visibility = View.GONE
            if (pTextView != null) {
                pTextView.visibility = View.GONE
            }
        } else {
            try {
                controlPicture(pContext, pURL, pImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (pTextView != null) {
                pTextView.text = pURL
            }
        }
    }

    /**
     * 二三四張圖片用這個tool
     *
     * @param pContext
     * @param pURL
     * @param pImageView
     * @param pTextView
     */
    @Throws(Exception::class)
    fun setPictureGone(
        pContext: Context?,
        pURL: String,
        pImageView: ImageView,
        pTextView: TextView?
    ) {
        if (pURL == "") {
            pImageView.visibility = View.GONE
            if (pTextView != null) {
                pTextView.visibility = View.GONE
            }
        } else {
            controlPicture(pContext, pURL, pImageView)
            if (pTextView != null) {
                pTextView.text = pURL
            }
        }
    }


    /**
     * 設定地理位置名稱
     */
    @Suppress("NAME_SHADOWING")
    fun setGeo(
        pListData: ListData,
        pLocationPositionData: LocationPositionData,
        pGeoListData: ArrayList<LocationPositionData>
    ) {
        //"熱帶雨林室內館(穿山甲館)；兩棲爬蟲動物館",
        //"沙漠動物區；兒童動物區"
        var pLocationPositionData: LocationPositionData = pLocationPositionData
        val iGeo: String? = pListData.getKeyLocation()
        val iGeoSplit = iGeo?.split("；")?.toTypedArray()
        var iGeoStore: String
        if (iGeoSplit != null) {
            for (i in iGeoSplit.indices) {
                iGeoStore = iGeoSplit[i].replace("\"".toRegex(), "")
                pLocationPositionData.setKeyLocationLogo(iGeoStore)
                pGeoListData.add(pLocationPositionData)
                pLocationPositionData = LocationPositionData()
            }
        }
    }

    /**
     * 設定位置的Map
     */
    @Suppress("NAME_SHADOWING")
    fun setLocation(
        pListData: ListData,
        pLocationPositionData: LocationPositionData,
        pLocationPositionDataArrayList: ArrayList<LocationPositionData>
    ) {
        //"MULTIPOINT ((121.5804577 24.9979216), (121.5805328 24.9959671), (121.5836763 24.9957094), (121.5894029 24.9951126), (121.5899205 24.9945669))",
        var pLocationPositionData: LocationPositionData = pLocationPositionData
        var iSplit: Array<String?>
        var iCount = 0
        val iLocation: String = pListData.getKeyGeo()
            ?.replace("MULTIPOINT ((", "")
            ?.replace("))", "")
            ?.replace("),   (", "-")
            ?.replace("),  (", "-")
            ?.replace("), (", "-").toString()
        //((121.5833766 24.9960938),(121.5898494 24.9940697))
        val iLocationStore = iLocation.split("-").toTypedArray()
        for (i in iLocationStore.indices) {
            iSplit = iLocationStore[i].split(" ").toTypedArray()
            pLocationPositionData.setKeyXPosition(iSplit[iCount])
            pLocationPositionData.setKeyYPosition(iSplit[iCount + 1])
            pLocationPositionDataArrayList.add(pLocationPositionData)
            pLocationPositionData = LocationPositionData()
            iCount = 0
        }
    }

    /**
     * 沒有資料就把他GONE起來
     */
    fun setData(pShowContext: String?, pTextView: TextView) {
        if (pShowContext == null) {
            pTextView.visibility = View.GONE
        } else {
            pTextView.text = pShowContext
        }
    }


    /**
     * 在ScrollView中滑動TextView方法
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setTextScrollView(pTextView: TextView) {
        pTextView.movementMethod = ScrollingMovementMethod.getInstance()
        pTextView.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_MOVE -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }
}