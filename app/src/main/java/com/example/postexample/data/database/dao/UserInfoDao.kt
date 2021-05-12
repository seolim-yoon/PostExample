package com.example.postexample.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.postexample.data.database.entity.User

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Insert
    fun insert(video: User)

    @Delete
    fun delete(video: User)
}