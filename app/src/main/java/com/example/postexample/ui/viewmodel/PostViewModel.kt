package com.example.postexample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postexample.data.repository.PostRepository
import com.example.postexample.model.PostInfo
import com.example.postexample.ui.base.BaseViewModel
import com.example.postexample.util.LoginPreference
import com.example.postexample.util.TimeFormatUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class PostViewModel(application: Application): BaseViewModel(application) {
    private val postRepository = PostRepository(application)

    var uri: MutableLiveData<String> = MutableLiveData()
    var title: MutableLiveData<String> = MutableLiveData()
    var content: MutableLiveData<String> = MutableLiveData()

    var clickImage: MutableLiveData<Boolean> = MutableLiveData()
    var postInfo: MutableLiveData<PostInfo> = MutableLiveData()
    var uploadState: MutableLiveData<ResultState> = MutableLiveData()
    var deletePosition: MutableLiveData<Int> = MutableLiveData()

    fun clickImageBtn() {
        clickImage.value = true
    }

    fun createPost(uri: String, title: String, content: String) {
        showLoadingBar()
        addDisposable(
            postRepository.createPost(uri, title, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uploadState.value = ResultState.SUCCESS
                    viewModelScope.launch {
                        postRepository.insertPost(uri, title, content, LoginPreference.getUserName(), TimeFormatUtils.dateFormat.format(Date(System.currentTimeMillis())))
                    }
                    refreshPost()
                    hideLoadingBar()

                }, {
                    uploadState.value = ResultState.FAIL
                    hideLoadingBar()
                })
        )
    }

    fun removePost(position: Int, uri: String, title: String) {
        addDisposable(
                postRepository.removePost(position, title)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ position ->
                            deletePosition.value = position
                            viewModelScope.launch {
                                postRepository.deletePost(uri)
                            }
                        }, {

                        })
        )
    }

    fun deletePost() {

    }

    fun refreshPost() {
        addDisposable(
            postRepository.refreshPost()
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

    fun loadImageURL(post: HashMap<String, String>) {
        addDisposable(
            postRepository.loadImageURL(post["title"] ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uri ->
                    postInfo.value = PostInfo(uri.toString(),
                                                    post["title"],
                                                    post["content"],
                                                    post["name"],
                                                    post["date"])
                }, {

                })
        )
    }
}

enum class ResultState(state: String) {
    SUCCESS("success"),
    FAIL("fail");
}
