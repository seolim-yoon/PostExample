package com.example.postexample.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postexample.data.repository.LoginRepository
import com.example.postexample.ui.base.BaseViewModel
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
                    .subscribe({ result ->
                        viewModelScope.launch {
                            LoginPreference.setUserPreference(loginRepository.getCurrentUser().name, email, pw)
                        }
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
