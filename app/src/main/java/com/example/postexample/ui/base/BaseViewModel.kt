package com.example.postexample.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postexample.data.database.entity.User
import com.example.postexample.data.repository.LoginRepository
import com.example.postexample.data.repository.PostRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    open fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    open fun showLoadingBar() {
        isLoading.value = true
    }

    open fun hideLoadingBar() {
        isLoading.value = false
    }
}