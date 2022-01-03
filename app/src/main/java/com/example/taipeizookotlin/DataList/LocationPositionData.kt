package com.example.taipeizookotlin.DataList

import java.io.Serializable

class LocationPositionData : Serializable {
    private var mKeyXPosition: String? = null
    private var mKeyYPosition: String? = null
    private var mKeyLocationLogo: String? = null


    fun setKeyLocationLogo(keyLocationLogo: String?) {
        mKeyLocationLogo = keyLocationLogo
    }

    fun getKeyLocationLogo(): String? {
        return if (mKeyLocationLogo != null) {
            mKeyLocationLogo
        } else {
            ""
        }
    }

    fun getKeyXPosition(): String? {
        return mKeyXPosition
    }

    fun setKeyXPosition(keyX_position: String?) {
        mKeyXPosition = keyX_position
    }


    fun getKeyYPosition(): String? {
        return mKeyYPosition
    }

    fun setKeyYPosition(keyY_position: String?) {
        mKeyYPosition = keyY_position
    }

}