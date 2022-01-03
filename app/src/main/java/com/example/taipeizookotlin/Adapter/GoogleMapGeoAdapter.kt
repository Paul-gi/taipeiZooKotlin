package com.example.taipeizookotlin.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.R
import java.util.ArrayList

class GoogleMapGeoAdapter : RecyclerView.Adapter<GoogleMapGeoAdapter.MyViewHolder>() {
    private val mGeoDataList: ArrayList<LocationPositionData> = ArrayList<LocationPositionData>()


//    fun GoogleMapGeoAdapter(pGeoList: ArrayList<LocationPositionData>?) {
//        setData(pGeoList)
//    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(pLocationDataList: ArrayList<LocationPositionData>?) {
        mGeoDataList.clear()
        mGeoDataList.addAll(pLocationDataList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.googlemap_geo_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.mALocation.text = mGeoDataList[position].getKeyLocationLogo()
    }

    override fun getItemCount(): Int {
        return mGeoDataList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mALocation: TextView = itemView.findViewById(R.id.mA_Location)

    }
}