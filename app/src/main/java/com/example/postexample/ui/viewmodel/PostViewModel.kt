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
import com.example.postexample.ui.base.BaseViewModel
import com.google.firebase.database.DataSnapshot
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.HashMap

class PostViewModel(application: Application): BaseViewModel(application) {
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

    val pager = Pager(PagingConfig(pageSize = 10)) {
        Log.v("seolim", "allTodos : " + allPosts.value.toString())
        PostPagingSource(allPosts.value ?: listOf())
    }.flow.map {
        it.map<DataModel> { DataModel.PostInfo(it.url, it.title, it.content, it.name, it.date) }
//                .insertHeaderItem(DataModel.Header("HEADER"))
//                .insertFooterItem(DataModel.Header("FOOTER"))

    }.cachedIn(viewModelScope)

    fun getAllPosts() {
        addDisposable(postRepository.getAllPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Post>>() {
                    override fun onSuccess(posts: List<Post>) {
                        allPosts.value = posts
                        Log.e("seolim", "set")
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
                .subscribe({
                    it.children.forEach {
                        loadImageURL(it.getValue() as HashMap<String, String>)
                        Log.i("seolim", "value : " + (it.getValue() as HashMap<String, String>).toString())
                    }
                }, {
                })
        )
    }

    fun createPost(uri: String, title: String, content: String) {
        showLoadingBar()
        addDisposable(
            postRepository.createPost(uri, title, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uploadState.value = ResultState.SUCCESS
                    changePost()
                    hideLoadingBar()

                }, {
                    uploadState.value = ResultState.FAIL
                    hideLoadingBar()
                })
        )
    }

    fun removePost(position: Int, title: String) {
        showLoadingBar()
        addDisposable(
            postRepository.removePost(position, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ position ->
                    deleteState.value = ResultState.SUCCESS
                    changePost()
                }
        )
    }

    fun loadImageURL(post: HashMap<String, String>) {
        addDisposable(
            postRepository.loadImageURL(post["title"] ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { uri ->
                    var post = Post(uri.toString(),
                            post["title"] ?: "",
                            post["content"] ?: "",
                            post["name"] ?: "",
                            post["date"] ?: "")

                    viewModelScope.launch {
                        postRepository.insertPost(post)
                    }
                    getAllPosts()
                }
        )
    }

    // add, delete
    fun changePost() {
        addDisposable(
            postRepository.changePost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    it.forEach {
                    }
                    when {
                        it.containsKey("add") -> loadImageURL(it.get("add")?.getValue() as HashMap<String, String>)
                        it.containsKey("remove") -> {
                            var post = it.get("remove")?.getValue() as HashMap<String, String>
                            viewModelScope.launch {
                                postRepository.deletePost(post["date"] ?: "")
                            }
                            hideLoadingBar()
                            getAllPosts()
                        }
                    }
                }
        )
    }
}

enum class ResultState(state: String) {
    SUCCESS("success"),
    FAIL("fail");
}
