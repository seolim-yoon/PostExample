package com.example.postexample.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.postexample.data.database.entity.Post
import java.util.concurrent.Flow

@Dao
interface PostInfoDao {
    @Query("SELECT * FROM post")
    fun getAllPost(): LiveData<List<Post>>

    @Insert
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)
}