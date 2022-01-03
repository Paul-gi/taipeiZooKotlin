@file:Suppress("PackageName")

package com.example.taipeizookotlin.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Util.ProgressDialogCustom
import com.example.taipeizookotlin.Util.UtilCommonStr

abstract class BaseFragment<dataBinding : ViewDataBinding> : Fragment() {
    private var mTampDataBinding: dataBinding? = null
    protected val mDataBinding: dataBinding get() = mTampDataBinding!!
    var mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
    var mTitleStr = "Title"
    var mProgressDialogCustom: ProgressDialogCustom? = null
    abstract val mLayout: Int


    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val iDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            mLayout,
            container,
            false
        )
        mTampDataBinding = iDataBinding as dataBinding?

        initView()
        return iDataBinding.root
    }


    protected open fun initView() {
        mProgressDialogCustom = ProgressDialogCustom(requireContext())
        checkBundle()
    }

    protected fun getTitleName(): String {
        return mTitleStr
    }


    private fun checkBundle() {
        val iBundle = arguments
        if (iBundle != null) {
            mTitleStr = iBundle.getString(mUtilCommonStr.mKeyTitle, "")
        }
    }

    fun goToNextPage(pFragment: Fragment, pTypeStr: String?) {
        val iBundle = Bundle()
        iBundle.putString(mUtilCommonStr.mKeyTitle, pTypeStr)

        parentFragmentManager.beginTransaction()
            .add(R.id.mFragment, pFragment.javaClass, iBundle, pFragment.javaClass.simpleName)
            .hide(this)
            .addToBackStack(null)
            .commit()
    }

    fun fragmentBackPressed() {
        parentFragmentManager.popBackStack()
    }
}