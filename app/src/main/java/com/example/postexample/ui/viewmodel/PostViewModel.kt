package com.example.postexample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.postexample.data.PostPagingSource
import com.example.postexample.data.database.entity.Post
import com.example.postexample.data.repository.PostRepository
import com.example.postexample.model.DataModel
import com.example.postexample.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.map

class PostViewModel(application: Application) : BaseViewModel(application) {
    private val postRepository = PostRepository(application)

    var uri: MutableLiveData<String> = MutableLiveData()
    var title: MutableLiveData<String> = MutableLiveData()
    var content: MutableLiveData<String> = MutableLiveData()

    var clickImage: MutableLiveData<Boolean> = MutableLiveData()
    var uploadState: MutableLiveData<ResultState> = MutableLiveData()
    var deleteState: MutableLiveData<ResultState> = MutableLiveData()

    var allPosts: MutableLiveData<List<Post>> = MutableLiveData()

    fun clickImageBtn() {
        clickImage.value = true
    }

    val pager = Pager(PagingConfig(pageSize = 6)) {
        Log.v("seolim", "allTodos : " + allPosts.value.toString())
        PostPagingSource(allPosts.value ?: listOf())
    }.flow.map {
        it.map<DataModel> { DataModel.PostInfo(it.url, it.title, it.content, it.name, it.date, it.likenum) }
//                .insertHeaderItem(DataModel.Header("HEADER"))
//                .insertFooterItem(DataModel.Header("FOOTER"))

    }.cachedIn(viewModelScope)

    private fun getAllPosts() {
        addDisposable(postRepository.getAllPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Post>>() {
                    override fun onSuccess(posts: List<Post>) {
                        allPosts.value = posts
                    }

                    override fun onError(e: Throwable) {
                        Log.e("seolim", "$e.message")
                    }
                }))
    }

    fun loadAllPost() {
        addDisposable(
                postRepository.loadAllPost()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { snapshot ->
                            getAllPosts()
                        }
        )
    }

    fun createPost(uri: String, title: String, content: String) {
        showLoadingBar()
        addDisposable(
                postRepository.createPost(uri, title, content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ post ->
                            val insertPost = Post(post["url"] ?: "",
                                    post["title"] ?: "",
                                    post["content"] ?: "",
                                    post["name"] ?: "",
                                    post["date"] ?: "",
                                    post["likenum"] ?: ""
                            )

                            insertRoomDB(insertPost)

                        }, {
                            Log.e("seolim", "error : " + it.message)
                            uploadState.value = ResultState.FAIL
                            hideLoadingBar()
                        })
        )
    }

    private fun insertRoomDB(insertPost: Post) {
        addDisposable(
                postRepository.insertPost(insertPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            uploadState.value = ResultState.SUCCESS
                            hideLoadingBar()
                            getAllPosts()
                        })
    }

    fun removePost(position: Int, title: String, date: String) {
        showLoadingBar()
        addDisposable(
                postRepository.removePost(position, title)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ position ->
                            removeRoomDB(date)
                        }, {
                            Log.e("seolim", "error : " + it.message)
                            deleteState.value = ResultState.FAIL
                            hideLoadingBar()
                        })
        )
    }

    private fun removeRoomDB(date: String) {
        addDisposable(
                postRepository.deletePostByDate(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            deleteState.value = ResultState.SUCCESS
                            hideLoadingBar()
                            getAllPosts()
                        })
    }
}

enum class ResultState(state: String) {
    NONE("none"),
    SUCCESS("success"),
    FAIL("fail");
}
