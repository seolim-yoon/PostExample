package com.example.postexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.postexample.data.repository.DataBaseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LogInViewModel : ViewModel() {
    private val repository = DataBaseRepository()

    var name: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pw: MutableLiveData<String> = MutableLiveData()

    var completeSignUp: MutableLiveData<LoginResult> = MutableLiveData()
    var completeLogIn: MutableLiveData<LoginResult> = MutableLiveData()

    private val disposable = CompositeDisposable()

    fun doSignUp(name: String, email: String, pw: String) {
        disposable.add(
            repository.doSignUp(name, email, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeSignUp.value = LoginResult.SUCCESS
                }, {
                    completeSignUp.value = LoginResult.FAIL
                })
        )
    }

    fun doLogIn(email: String, pw: String) {
        disposable.add(
            repository.doLogIn(email, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeLogIn.value = LoginResult.SUCCESS
                }, {
                    completeLogIn.value = LoginResult.FAIL
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}

enum class LoginResult(val state: String) {
    SUCCESS("Success"),
    FAIL("Fail");
}