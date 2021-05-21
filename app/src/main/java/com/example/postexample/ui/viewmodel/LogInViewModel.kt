package com.example.postexample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.postexample.data.repository.LoginRepository
import com.example.postexample.model.PostInfo
import com.example.postexample.model.UserInfo
import com.example.postexample.ui.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LogInViewModel(application: Application) : BaseViewModel(application) {
    private val repository = LoginRepository()

    var name: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pw: MutableLiveData<String> = MutableLiveData()

    var completeSignUp: MutableLiveData<LogInResult> = MutableLiveData()
    var completeLogIn: MutableLiveData<LogInResult> = MutableLiveData()
    var userState: MutableLiveData<UserState> = MutableLiveData()
    var getUserInfo: MutableLiveData<ResultState> = MutableLiveData()
    var userHashMap: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var userInfo: MutableLiveData<UserInfo> = MutableLiveData()

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
                        userState.value = UserState.LOGIN
                        hideLoadingBar()
                    }, {
                        completeLogIn.value = LogInResult.FAIL
                        hideLoadingBar()
                    })
        )
    }

    fun doLogOut() {
        repository.doLogOut()
        userState.value = UserState.LOGOUT
    }

    fun getUserInfo() {
        addDisposable(
                repository.getUserInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it.children.forEach {
                                val user = it.getValue() as HashMap<String, String>
                                userInfo.value = UserInfo(user["name"], user["email"], user["pw"])

                                Log.v("seolim", "key : " + it.key)
                                Log.v("seolim", "value : " + (it.getValue() as HashMap<String, String>).toString())
                            }
                            getUserInfo.value = ResultState.SUCCESS

                        }, {
                            getUserInfo.value = ResultState.FAIL
                        })
        )
    }
}

enum class UserState(val state: String) {
    LOGIN("Login"),
    LOGOUT("Logout");
}

enum class LogInResult(val result: String) {
    SUCCESS("Success"),
    FAIL("Fail");
}
