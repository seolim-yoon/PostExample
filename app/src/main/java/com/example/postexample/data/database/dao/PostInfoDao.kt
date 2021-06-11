package com.example.postexample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.postexample.data.database.entity.Post
import io.reactivex.Single

@Dao
interface PostInfoDao {
    @Query("SELECT * FROM post")
    fun getAllPost(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Query("DELETE FROM post WHERE url = :url")
    suspend fun deletePostByURL(url: String)
}