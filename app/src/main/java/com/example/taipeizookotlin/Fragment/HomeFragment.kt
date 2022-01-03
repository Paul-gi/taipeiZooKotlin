@file:Suppress("PackageName")

package com.example.taipeizookotlin.Fragment

import android.view.View
import com.example.taipeizookotlin.R
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.databinding.AllAreaNavigationBinding
import com.example.taipeizookotlin.databinding.DepartmentNavigationBinding
import com.example.taipeizookotlin.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val mLayout: Int
        get() = R.layout.home_fragment

    override fun initView() {
        super.initView()

        fistPage(mDataBinding.mAllAreaNavigationIC)
        secondPage(mDataBinding.mDepartmentNavigationIC)

        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener {
            if (mDataBinding.mToolbarLayout.root.visibility == View.VISIBLE) {
                openHomePage()
            } else {
                fragmentBackPressed()
            }
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

    private fun fistPage(pAllAreaNavigationIC: AllAreaNavigationBinding) {
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
        mDataBinding.mZooLogoImage.visibility = View.VISIBLE
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.VISIBLE

        mDataBinding.mToolbarLayout.root.visibility = View.GONE
        mDataBinding.mDepartmentNavigationIC.root.visibility = View.GONE
    }

    private fun openDepartmentPage() {
        mDataBinding.mZooLogoImage.visibility = View.GONE
        mDataBinding.mToolbarLayout.mToolbar.title = "館區簡介"
        mDataBinding.mToolbarLayout.mChange.visibility = View.GONE
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.GONE


        mDataBinding.mToolbarLayout.root.visibility = View.VISIBLE
        mDataBinding.mDepartmentNavigationIC.mInDoorAreaBtn.text = UtilCommonStr.getInstance().mInSideArea
        mDataBinding.mDepartmentNavigationIC.mOutDoorAreaBtn.text = UtilCommonStr.getInstance().mOutSideArea
        mDataBinding.mDepartmentNavigationIC.root.visibility = View.VISIBLE
    }
}