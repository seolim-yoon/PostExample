package com.example.postexample.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.postexample.data.database.entity.Post
import com.example.postexample.data.repository.PostRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PostPagingSource(private val repository: PostRepository): RxPagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Post>> {
        Log.v("seolim", "key : " + params.key)
        Log.d("seolim", "loadSize : " + params.loadSize)
        var position = params.key ?: 1
        return repository.getPagingData()
                .subscribeOn(Schedulers.io())
                .map<LoadResult<Int, Post>> { post ->
                    LoadResult.Page(
                            data = post,
                            prevKey = null,
                            nextKey = if (post.isNullOrEmpty()) null else position + 1)
                }.onErrorReturn {
                    LoadResult.Error(Throwable("Paging Error"))
                }
    }
}