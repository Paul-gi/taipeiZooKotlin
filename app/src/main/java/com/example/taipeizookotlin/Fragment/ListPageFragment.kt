@file:Suppress("PackageName")

package com.example.taipeizookotlin.Fragment

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taipeizookotlin.Adapter.ListDataAdapter
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Viewmodel.CallViewModel
import com.example.taipeizookotlin.databinding.FragmentListPageBinding

class ListPageFragment : BaseFragment<FragmentListPageBinding>() {
    private var mPageState = true
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mFinish = false

    private val mCallViewModel: CallViewModel by lazy {
        ViewModelProvider(this).get(CallViewModel::class.java)
    }

    private val mListDataAdapter: ListDataAdapter by lazy {
        ListDataAdapter(object : ListDataAdapter.ListDataItf {
            override fun getData(pListData: ListData?) {

            }

        }, requireContext(), mPageTitleStr, mPageState)
    }

    override val mLayout: Int
        get() = R.layout.fragment_list_page

    override fun initView() {
        super.initView()
        mProgressDialogCustom!!.show(parentFragmentManager, "")
        mLinearLayoutManager = LinearLayoutManager(this.activity)
        mDataBinding.mRecycleView.layoutManager = mLinearLayoutManager
        mDataBinding.mToolbarLayout.mToolbar.title = mPageTitleStr
        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener {
            fragmentBackPressed(this)
        }

        fragmentUseFcmBackPressed(this, this.requireActivity())

        mDataBinding.mRecycleView.adapter = mListDataAdapter
        mDataBinding.mToolbarLayout.mChange.setOnClickListener {
            if (!mPageState) {
                mGridLayoutManager = GridLayoutManager(activity, 1)
                mDataBinding.mRecycleView.layoutManager = mLinearLayoutManager
                mPageState = true
            } else {
                mGridLayoutManager = GridLayoutManager(activity, 2)
                mDataBinding.mRecycleView.layoutManager = mGridLayoutManager
                mPageState = false
            }
            mListDataAdapter.setPageState(mPageState)
            mDataBinding.mRecycleView.adapter = mListDataAdapter
        }


        //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝


        //================================RecycleView 到底刷新的部分＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        mDataBinding.mRecycleView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (!mDataBinding.mRecycleView.canScrollVertically(1)) {
                if (!mFinish) {
                    mProgressDialogCustom!!.show(parentFragmentManager, "")
                    callApiThread()
                } else {
                    Toast.makeText(activity, "到底了", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mCallViewModel.getDataFinishState().observe(viewLifecycleOwner) { aBoolean ->
            mFinish = aBoolean
        }
        mCallViewModel.getDataListObserver().observe(viewLifecycleOwner) { pCallData ->
            if (pCallData != null) {
                mListDataAdapter.setData(pCallData)
                mProgressDialogCustom!!.dismiss()
            }
        }
        callApiThread()
    }

    private fun callApiThread() {
        Thread { mCallViewModel.mCallApi(mPageTitleStr) }.start()
    }


}