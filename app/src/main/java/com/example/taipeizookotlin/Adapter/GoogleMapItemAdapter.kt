package com.example.taipeizookotlin.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.R
import java.util.*

class GoogleMapItemAdapter : RecyclerView.Adapter<GoogleMapItemAdapter.MyViewHolder>() {
    private val mLocationDataList: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()
    private var mMapViewRecycleViewClickListener: MapViewRecycleViewClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(
        pLocationList: ArrayList<LocationPositionData>?, pMapViewRecycleViewClickListener: MapViewRecycleViewClickListener?
    ) {
        mMapViewRecycleViewClickListener = pMapViewRecycleViewClickListener
        mLocationDataList.clear()
        mLocationDataList.addAll(pLocationList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.googlemap_recycleview_utem, parent, false)
        return MyViewHolder(view, mMapViewRecycleViewClickListener)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (mLocationDataList.size == 1) {
            holder.mItem.visibility = View.GONE
        }
        holder.mItem.text = "地點" + (position + 1)
    }

    override fun getItemCount(): Int {
        return mLocationDataList.size
    }


    class MyViewHolder(
        itemView: View,
        pMapViewRecycleViewClickListener: MapViewRecycleViewClickListener?
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mItem: TextView = itemView.findViewById(R.id.mLocationItem)
        private var mMapViewRecycleViewClickListener: MapViewRecycleViewClickListener =
            pMapViewRecycleViewClickListener!!

        override fun onClick(v: View) {
            mMapViewRecycleViewClickListener.onMapViewClicked(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface MapViewRecycleViewClickListener {
        fun onMapViewClicked(position: Int)
    }
}