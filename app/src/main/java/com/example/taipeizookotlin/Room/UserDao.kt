package com.example.taipeizookotlin.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * from user")
    fun getUserList(): List<User?>?

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    //    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    //    List<User> loadAllByIds(int[] userIds);
    //
    //    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
    //            "last_name LIKE :last LIMIT 1")
    //    User findByName(String first, String last);
    @Query("SELECT * FROM user WHERE PageNameTitle in(:type) AND click_position in(:positionID) AND EnglishName in(:EnglishName) AND ChineseName in(:ChineseName) ")
    fun findByName(
        type: String?,
        positionID: Int,
        EnglishName: String?,
        ChineseName: String?
    ): User?


    @Insert
    fun insertUser(users: User?)
//    @Delete
//    void delete(User user);
}