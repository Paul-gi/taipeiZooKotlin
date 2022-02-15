@file:Suppress("PackageName")

package com.example.taipeizookotlin.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.taipeizookotlin.Firebase.FirebaseService.Companion.mFcmFromInDepartmentBackPage
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.textviewSet.TextViewSetting
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.databinding.AllAreaNavigationBinding
import com.example.taipeizookotlin.databinding.DepartmentNavigationBinding
import com.example.taipeizookotlin.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeFragmentBinding>() {


    override val mLayout: Int
        get() = R.layout.home_fragment


    @SuppressLint("StringFormatInvalid")
    override fun initView() {
        super.initView()
        if (mFcmFromInDepartmentBackPage) {
            openDepartmentPage()
            mFcmFromInDepartmentBackPage = false
        }


        firstPage(mDataBinding.mAllAreaNavigationIC)
        secondPage(mDataBinding.mDepartmentNavigationIC)

        /**
         * UI的Back鍵
         */
        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener {
            if (mDataBinding.mToolbarLayout.root.visibility == View.VISIBLE) {
                openHomePage()
            } else {
                fragmentBackPressed(null)
            }
        }

        /**
         * 手機內建的back事件
         */
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mDataBinding.mToolbarLayout.root.visibility == View.VISIBLE) {
                        openHomePage()
                    } else {
                        fragmentBackPressed(null)
                    }
                }
            })

        mDataBinding.mAllAreaNavigationIC.mTextViewTest.setOnClickListener {
            openTextviewSettingPage(this.requireActivity())
        }
    }

    private fun secondPage(pDepartmentNavigationIC: DepartmentNavigationBinding) {
        pDepartmentNavigationIC.mInDoorAreaBtn.setOnClickListener {
            goToNextPage(
                ListPageFragment(),
                mUtilCommonStr.mInSideArea
            )
        }
        pDepartmentNavigationIC.mOutDoorAreaBtn.setOnClickListener {
            goToNextPage(
                ListPageFragment(),
                mUtilCommonStr.mOutSideArea
            )
        }
    }

    private fun firstPage(pAllAreaNavigationIC: AllAreaNavigationBinding) {
        pAllAreaNavigationIC.mDepartmentButton.setOnClickListener {
            openDepartmentPage()
        }

        pAllAreaNavigationIC.mAnimalButton.setOnClickListener {
            goToNextPage(
                ListPageFragment(),
                mUtilCommonStr.mAnimal
            )
        }
        pAllAreaNavigationIC.mPlantButton.setOnClickListener {
            goToNextPage(
                ListPageFragment(),
                mUtilCommonStr.mPlant
            )
        }
    }

    private fun openHomePage() {
       // mDataBinding.mZooLogoImage.visibility = View.VISIBLE
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.VISIBLE

        mDataBinding.mToolbarLayout.root.visibility = View.GONE
        mDataBinding.mDepartmentNavigationIC.root.visibility = View.GONE
    }

    private fun openDepartmentPage() {
        //mDataBinding.mZooLogoImage.visibility = View.GONE
        mDataBinding.mToolbarLayout.mToolbar.title = "館區簡介"
        mDataBinding.mToolbarLayout.mChange.visibility = View.GONE
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.GONE


        mDataBinding.mToolbarLayout.root.visibility = View.VISIBLE
        mDataBinding.mDepartmentNavigationIC.mInDoorAreaBtn.text =
            UtilCommonStr.getInstance().mInSideArea
        mDataBinding.mDepartmentNavigationIC.mOutDoorAreaBtn.text =
            UtilCommonStr.getInstance().mOutSideArea
        mDataBinding.mDepartmentNavigationIC.root.visibility = View.VISIBLE
    }


    private fun openTextviewSettingPage(mActivity: Activity) {
        val iIntent = Intent()
        iIntent.setClass(mActivity, TextViewSetting::class.java)
        startActivity(iIntent)
    }
}