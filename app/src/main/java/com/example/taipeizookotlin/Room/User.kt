package com.example.taipeizookotlin.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
 class User {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "PageNameTitle")
    var pageName: String? = null

    @ColumnInfo(name = "click_position")
    var clickPosition = 0

    @ColumnInfo(name = "EnglishName")
    var EnglishName: String? = null

    @ColumnInfo(name = "ChineseName")
    var ChineseName: String? = null



//        fun User(pPageName: String?, position: Int, pEnglishName: String?, pChineseName: String?) {
//            pageName = pPageName
//            clickPosition = position
//            EnglishName = pEnglishName
//            ChineseName = pChineseName
//        }

//        fun User() {}
}