package com.example.postexample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.postexample.data.repository.PostRepository
import com.example.postexample.model.PostInfo
import com.example.postexample.ui.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PostViewModel(application: Application): BaseViewModel(application) {
    private val repository = PostRepository(application)

    var uri: MutableLiveData<String> = MutableLiveData()
    var title: MutableLiveData<String> = MutableLiveData()
    var content: MutableLiveData<String> = MutableLiveData()

    var clickImage: MutableLiveData<Boolean> = MutableLiveData()
    var postInfo: MutableLiveData<PostInfo> = MutableLiveData()
    var uploadState: MutableLiveData<ResultState> = MutableLiveData()

    fun clickImageBtn() {
        clickImage.value = true
    }

    fun createPost(uri: String, title: String, content: String) {
        showLoadingBar()
        addDisposable(
            repository.createPost(uri, title, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uploadState.value = ResultState.SUCCESS
                    refreshPost()
                    hideLoadingBar()

                }, {
                    uploadState.value = ResultState.FAIL
                    hideLoadingBar()
                })
        )
    }

    fun deletePost() {

    }

    fun refreshPost() {
        addDisposable(
            repository.refreshPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.children.forEach {
                        loadImageURL(it.getValue() as HashMap<String, String>)

                        Log.i("seolim", "key : " + it.key ?: "")
                        Log.i("seolim", "value : " + (it.getValue() as HashMap<String, String>).toString())
                    }
                }, {

                })
        )
    }

    fun loadImageURL(post: HashMap<String, String>) {
        addDisposable(
            repository.loadImageURL(post["title"] ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uri ->
                    postInfo.value = PostInfo(uri.toString(), post["title"], post["content"])
                }, {

                })
        )
    }
}

enum class ResultState(state: String) {
    SUCCESS("success"),
    FAIL("fail");
}
