@file:Suppress("PackageName")

package com.example.taipeizookotlin.Fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFcmFromInDepartmentBackPage
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Util.ProgressDialogCustom
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

abstract class BaseFragment<dataBinding : ViewDataBinding> : Fragment() {
    private var mTampDataBinding: dataBinding? = null
    protected val mDataBinding: dataBinding get() = mTampDataBinding!!
    var mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
    var mPageTitleStr = "Title"
    var mProgressDialogCustom: ProgressDialogCustom? = null
    abstract val mLayout: Int
    var mFormFirebase = false
    //mInDepartmentPage 當推播去列表頁,點選回上一頁時要讓他正常回上一頁


    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic("news")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("firebaseToken", token.toString())
        })


    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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


    private fun checkBundle() {
        val iBundle = arguments
        if (iBundle != null) {
            mFormFirebase = iBundle.getBoolean("MainFromFireBase", false)

            mPageTitleStr = if (mFormFirebase) {
                iBundle.getString("MainFirebasePageTitle", "")
            } else {
                iBundle.getString(mUtilCommonStr.mKeyTitle, "")
            }
        }


        //推播導頁
        if (mPageTitleStr != "" || !mPageTitleStr.equals(null)) {
            when (mPageTitleStr) {
                "Animal" -> mPageTitleStr = mUtilCommonStr.mAnimal
                "Plant" -> mPageTitleStr = mUtilCommonStr.mPlant
                "OutSideArea" -> {
                    mPageTitleStr = mUtilCommonStr.mOutSideArea
                    mFcmFromInDepartmentBackPage = true
                }
                "InSideArea" -> {
                    mPageTitleStr = mUtilCommonStr.mInSideArea
                    mFcmFromInDepartmentBackPage = true
                }
            }
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


    fun fragmentBackPressed(pFragment: Fragment?) {
        if (mPageTitleStr != "") {
            if (pFragment != null) {
                onBackPressOpenHomePage(pFragment)
            }
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    fun fragmentUseFcmBackPressed(pFragment: Fragment, pFragmentActivity: FragmentActivity) {
        pFragmentActivity
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressOpenHomePage(pFragment)
                    /**
                     * if you want onBackPressed() to be called as normal afterwards
                     */
//                    if (isEnabled) {
//                        isEnabled = false
//                        requireActivity().onBackPressed()
//                    }
                }
            })

    }

    fun onBackPressOpenHomePage(pFragment: Fragment) {
        if (mPageTitleStr != "") {
            mPageTitleStr = ""
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.mFragment,
                    HomeFragment().javaClass,
                    null,
                    HomeFragment().javaClass.simpleName
                )
                .addToBackStack(null)
                .remove(pFragment)
                .commit()
            //pFragment.activity?.finish()

//        } else {
            // fragmentBackPressed(parentFragment)

        } else {
            parentFragmentManager.popBackStack()
        }
    }
}