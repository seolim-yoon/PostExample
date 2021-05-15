package com.example.postexample.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.postexample.data.repository.PostRepository
import com.example.postexample.ui.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class PostViewModel(): BaseViewModel() {
    private val repository = PostRepository()

    var url: MutableLiveData<String> = MutableLiveData()
    var title: MutableLiveData<String> = MutableLiveData()
    var content: MutableLiveData<String> = MutableLiveData()

    var clickImage: MutableLiveData<Boolean> = MutableLiveData()
    var uploadState: MutableLiveData<UploadState> = MutableLiveData()
    var refreshPost: MutableLiveData<RefreshPost> = MutableLiveData()

    fun clickImageBtn() {
        clickImage.value = true
    }

    fun createPost(url: String, title: String, content: String) {
        try {
            repository.createPost(url, title, content)
            uploadState.value = UploadState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            uploadState.value = UploadState.FAIL
        }
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
                        val key = it.key
                        Log.i("seolim", "key : " + key)
                        val value = it.getValue()
                        Log.i("seolim", "value : " + value.toString())
                    }
                    refreshPost.value = RefreshPost.SUCCESS

                }, {
                    refreshPost.value = RefreshPost.FAIL
                })
        )
    }
}

enum class UploadState(state: String) {
    SUCCESS("success"),
    FAIL("fail");
}

enum class RefreshPost(state: String) {
    SUCCESS("success"),
    FAIL("fail");
}