package com.example.postexample.data.database.dao

import androidx.room.*
import com.example.postexample.data.database.entity.Post
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostInfoDao {
    @Query("SELECT * FROM post ORDER BY date DESC")
    fun getAllPost(): Single<List<Post>>

    @Query("SELECT * FROM post LIMIT :perPage")
    fun getPostList(perPage: Int): Single<List<Post>>

    @Query("SELECT * FROM post WHERE date = :date")
    fun getPostByDate(date: String): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post) : Completable

    @Query("DELETE FROM post WHERE date = :date")
    fun deletePostByDate(date: String) : Completable

    @Query("DELETE FROM post")
    fun deleteAll()
}