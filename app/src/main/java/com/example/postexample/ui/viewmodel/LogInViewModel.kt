package com.example.postexample.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.postexample.data.repository.DataBaseRepository
import com.example.postexample.ui.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LogInViewModel : BaseViewModel() {
    private val repository = DataBaseRepository()

    var name: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pw: MutableLiveData<String> = MutableLiveData()

    var completeSignUp: MutableLiveData<LogInResult> = MutableLiveData()
    var completeLogIn: MutableLiveData<LogInResult> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun doSignUp(name: String, email: String, pw: String) {
        showLoadingBar()
        addDisposable(
            repository.doSignUp(name, email, pw)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        completeSignUp.value = LogInResult.SUCCESS
                        hideLoadingBar()
                    }, {
                        completeSignUp.value = LogInResult.FAIL
                        hideLoadingBar()
                    })
        )
    }

    fun doLogIn(email: String, pw: String) {
        showLoadingBar()
        addDisposable(
            repository.doLogIn(email, pw)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        completeLogIn.value = LogInResult.SUCCESS
                        hideLoadingBar()
                    }, {
                        completeLogIn.value = LogInResult.FAIL
                        hideLoadingBar()
                    })
        )
    }

    fun showLoadingBar() {
        isLoading.value = true
    }

    fun hideLoadingBar() {
        isLoading.value = false
    }
}

enum class LogInResult(val state: String) {
    SUCCESS("Success"),
    FAIL("Fail");
}
