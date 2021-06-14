package com.example.postexample.data.database.dao

import androidx.room.*
import com.example.postexample.data.database.entity.Post
import io.reactivex.Single

@Dao
interface PostInfoDao {
    @Query("SELECT * FROM post")
    fun getAllPost(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(post: Post)

    @Query("DELETE FROM post WHERE date = :date")
    suspend fun deletePostByDate(date: String)

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}