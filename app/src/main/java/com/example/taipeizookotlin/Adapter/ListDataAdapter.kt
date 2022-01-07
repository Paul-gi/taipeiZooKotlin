@file:Suppress("PackageName")

package com.example.taipeizookotlin.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.DetailActivity
import com.example.taipeizookotlin.Firebase.FirebaseService
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Room.AppDataBase
import com.example.taipeizookotlin.Room.User
import com.example.taipeizookotlin.Util.UtilTools
import java.lang.Exception
import java.util.ArrayList

class ListDataAdapter(
    private val mListDataItf: ListDataItf,
    private val context: Context,
    private val mTitleName: String,
    private var mPageState: Boolean,
) : RecyclerView.Adapter<ListDataAdapter.MyViewHolder>() {
    private val mZooDataList: ArrayList<ListData> = ArrayList<ListData>()
    private val mAlreadyRead = ArrayList<Int>()
    private val mSynchronizedUsed = "aa"

    @Suppress("DEPRECATION")
    private val mHandler = Handler()
    private val mRunnable = Runnable { changeUI() }
    private val mUtilTools: UtilTools = UtilTools()


    fun setPageState(pPageState: Boolean) {
        mPageState = pPageState
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(pAnimalDataList: ArrayList<ListData>?) {
        mZooDataList.addAll(pAnimalDataList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = if (mPageState) {
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_item, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_horizontal_item, parent, false)
        }
        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        val iListData: ListData = mZooDataList[position]
        try {
            iListData.keyUrl01()?.let { mUtilTools.controlPicture(context, it, holder.mPic01URL) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        iListData.getEnglishName()?.let { getRoom(position, it, iListData.getChineseName()!!) }
        setData(iListData.getChineseName(), holder.mNameCh)
        setData(iListData.getEnglishName(), holder.mNameEn)
        if (mAlreadyRead.contains(position)) {
            holder.itemView.setBackgroundResource(R.color.gold)
        } else {
            holder.itemView.setBackgroundResource(0)
        }
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mClick)
    }

    private val mClick = View.OnClickListener { v ->
        if (v.tag != null) {
            val iIndex: Int
            val iD = v.tag.toString()
            if (Character.isDigit(iD[0])) {
                iIndex = iD.toInt()
                throwData(this.context, iIndex)
                mListDataItf.getData(mZooDataList[iIndex])
                setRoom(iIndex)
                v.setBackgroundResource(R.color.gold)
            }
        }
    }

    private fun setData(pShowContext: String?, pTextView: TextView) {
        if (pShowContext == null) {
            pTextView.visibility = View.GONE
        } else {
            pTextView.text = pShowContext
        }
    }

    override fun getItemCount(): Int {
        return mZooDataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameCh: TextView = itemView.findViewById(R.id.mName_Ch)
        var mNameEn: TextView = itemView.findViewById(R.id.mName_En)
        var mPic01URL: ImageView = itemView.findViewById(R.id.mPic01_URL)

    }

    private fun throwData(pContent: Context?, pPosition: Int) {
        val iData: ListData = mZooDataList[pPosition]
        val iIntent = Intent()
        val iBundle = Bundle()
        iIntent.setClass(pContent!!, DetailActivity::class.java)
        iBundle.putString("TitleName", mTitleName)
        iBundle.putString("ListData", iData.getRawData())
        iIntent.putExtras(iBundle)
        context.startActivity(iIntent)
    }

    interface ListDataItf {
        fun getData(pListData: ListData?)
    }


    private fun getRoom(pPosition: Int, pEnglishName: String, pChineseName: String) {
        Thread {
            synchronized(mSynchronizedUsed) {
                val iFindData = mAlreadyRead.indexOf(pPosition)
                if (iFindData == -1) {
                    val ix = AppDataBase.getInstance(context)!!
                        .userDao().findByName(mTitleName, pPosition, pEnglishName, pChineseName)
                    if (ix != null) {
                        mAlreadyRead.add(pPosition)
                        mHandler.removeCallbacks(mRunnable)
                        mHandler.postDelayed(mRunnable, 500)
                    }
                }
            }
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeUI() {
        notifyDataSetChanged()
    }

    private fun setRoom(pPosition: Int) {
        Thread {

//                AppDataBase db = Room.databaseBuilder(context, AppDataBase.class, "clickStore").build();
            val iListData: ListData = mZooDataList[pPosition]
            //查詢
            val mApDataBase: AppDataBase? = context.let { AppDataBase.getInstance(it) }
            //            mApDataBase.userDao().getUserList();

            //插入
            mApDataBase?.userDao()?.insertUser(
                User().apply {
                    pageName = mTitleName
                    clickPosition = pPosition
                    EnglishName = iListData.getEnglishName()
                    ChineseName = iListData.getChineseName()
                }
            )
        }.start()
    }
}