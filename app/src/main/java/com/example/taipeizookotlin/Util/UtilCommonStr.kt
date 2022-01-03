@file:Suppress("PackageName")

package com.example.taipeizookotlin.Util

class UtilCommonStr {

    companion object {
        private var mUtilCommonStr: UtilCommonStr? = null

        fun getInstance(): UtilCommonStr {

            mUtilCommonStr?.let {
                return it
            } ?: kotlin.run {
                val iU = UtilCommonStr()
                mUtilCommonStr = iU
                return iU
            }
        }
    }


    //    public String mDepartment = "Department";
    var mAnimal = "動物簡介"
    var mPlant = "植物簡介"
//    var mKeyRawJson = "KeyRawJson"

    var mInSideArea = "室內區"
    var mOutSideArea = "戶外區"

    var mKeyTitle = "mKeyTitle"
}