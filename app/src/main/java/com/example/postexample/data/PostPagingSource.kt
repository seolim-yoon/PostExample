package com.example.postexample.data

import androidx.paging.PagingSource
import com.example.postexample.data.database.entity.Post
import java.lang.Exception

class PostPagingSource(private val post: List<Post>): PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            LoadResult.Page(
                    data = post,
                    prevKey = null,
                    nextKey = null,
            )
        } catch (e: Exception) {
            LoadResult.Error(Throwable("Paging Error"))
        }
    }
}