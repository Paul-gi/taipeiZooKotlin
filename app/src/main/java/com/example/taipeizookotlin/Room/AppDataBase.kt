package com.example.taipeizookotlin.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    companion object {
        var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "user_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }


    abstract fun userDao(): UserDao

}