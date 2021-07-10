package com.example.postexample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postexample.data.repository.LoginRepository
import com.example.postexample.base.BaseViewModel
import com.example.postexample.util.LoginPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class LogInViewModel(application: Application) : BaseViewModel(application) {
    private val loginRepository = LoginRepository(application)

    var name: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pw: MutableLiveData<String> = MutableLiveData()

    var completeSignUp: MutableLiveData<LogInResult> = MutableLiveData()
    var completeLogIn: MutableLiveData<LogInResult> = MutableLiveData()
    var userState: MutableLiveData<UserState> = MutableLiveData()

    fun loadAllUser() {
        addDisposable(
            loginRepository.loadAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.children.forEach {
                        val user = it.getValue() as HashMap<String, String>
                        viewModelScope.launch {
                            loginRepository.insertUser(user["name"] ?: "", user["email"] ?: "", user["pw"] ?: "")
                        }
                        Log.i("seolim", "value : " + (it.getValue() as HashMap<String, String>).toString())
                    }
                }, {
                })
        )
    }

    fun doSignUp(name: String, email: String, pw: String) {
        showLoadingBar()
        addDisposable(
            loginRepository.doSignUp(name, email, pw)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        completeSignUp.value = LogInResult.SUCCESS
                        viewModelScope.launch {
                            loginRepository.insertUser(name, email, pw)
                        }
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
            loginRepository.doLogIn(email, pw)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ email ->
                        viewModelScope.launch {
                            LoginPreference.setUserPreference(loginRepository.getCurrentUser(email).name, email, pw)
                            completeLogIn.value = LogInResult.SUCCESS
                            userState.value = UserState.LOGIN
                        }
                        hideLoadingBar()
                    }, {
                        completeLogIn.value = LogInResult.FAIL
                        hideLoadingBar()
                    })
        )
    }

    fun doLogOut() {
        loginRepository.doLogOut()
        userState.value = UserState.LOGOUT
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
