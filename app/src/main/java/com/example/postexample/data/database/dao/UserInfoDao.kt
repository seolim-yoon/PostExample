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
    fun getAllUser(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE email LIKE :email")
    suspend fun getCurrentUser(email: String): User

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user WHERE email = :email")
    fun deleteUserByEmail(email: String): Int
}