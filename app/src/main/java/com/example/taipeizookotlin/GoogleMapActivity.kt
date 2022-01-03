package com.example.taipeizookotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taipeizookotlin.Adapter.GoogleMapItemAdapter
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.databinding.FragmentGoogleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMapItemAdapter.MapViewRecycleViewClickListener {
    private lateinit var mFragmentGoogleMapBinding: FragmentGoogleMapBinding
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mLatLng: LatLng
    private var mLocationPositionListData: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentGoogleMapBinding =
            DataBindingUtil.setContentView(this, R.layout.fragment_google_map)
        getBundle()
        init()
    }


    private fun init() {
        mFragmentGoogleMapBinding.mGoogleMapRecycleView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val mGoogleMapItemAdapter = GoogleMapItemAdapter()
        mGoogleMapItemAdapter.setData(mLocationPositionListData,this)
        mFragmentGoogleMapBinding.mGoogleMapRecycleView.adapter = mGoogleMapItemAdapter
        val iSupportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.mGoogleMap) as SupportMapFragment?)!!
        iSupportMapFragment.getMapAsync(this)
    }

    private fun getBundle() {
        val iBundle = intent.extras
        if (iBundle != null) {
            mLocationPositionListData =
                iBundle.getSerializable("mLocationPositionListData") as ArrayList<LocationPositionData>
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mGoogleMap = googleMap
        }
        mLatLng = LatLng(
            mLocationPositionListData[0].getKeyYPosition()!!.toDouble(),
            mLocationPositionListData[0].getKeyXPosition()!!.toDouble()
        )
        mGoogleMap.addMarker(MarkerOptions().position(mLatLng))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))


//        //放大地圖到15倍
//        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //設定 右下角的放大縮小功能
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        //設定 左上角的指南針，要兩指旋轉才會出現
        mGoogleMap.uiSettings.isCompassEnabled = true
        //設定 右下角的導覽及開啟 Google Map功能
        mGoogleMap.uiSettings.isMapToolbarEnabled = true
    }


    override fun onMapViewClicked(position: Int) {
        mGoogleMap.clear()
        mLatLng = LatLng(
            mLocationPositionListData[position].getKeyYPosition()!!.toDouble(),
            mLocationPositionListData[position].getKeyXPosition()!!.toDouble()
        )
        mGoogleMap.addMarker(MarkerOptions().position(mLatLng))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
    }
}