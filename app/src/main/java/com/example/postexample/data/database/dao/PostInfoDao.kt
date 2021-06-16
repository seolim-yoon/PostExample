package com.example.postexample.data.database.dao

import androidx.room.*
import com.example.postexample.data.database.entity.Post
import io.reactivex.Single

@Dao
interface PostInfoDao {
    @Query("SELECT * FROM post ORDER BY date DESC")
    fun getAllPost(): Single<List<Post>>

    @Query("SELECT * FROM post WHERE date = :date")
    fun getPostByDate(date: String): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(post: Post)

    @Query("DELETE FROM post WHERE date = :date")
    suspend fun deletePostByDate(date: String)

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}