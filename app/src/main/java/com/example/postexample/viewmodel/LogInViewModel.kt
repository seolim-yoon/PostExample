package com.example.postexample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.postexample.data.repository.DataBaseRepository

class LogInViewModel: ViewModel() {
    private val repository = DataBaseRepository()

    var name: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pw: MutableLiveData<String> = MutableLiveData()

    var completeSignUp: MutableLiveData<String> = MutableLiveData()
    var completeLogIn: MutableLiveData<String> = MutableLiveData()

    fun doSignUp(name: String, email: String, pw: String) {
        repository.doSignUp(name, email, pw).let {
            when(it) {
                true -> completeSignUp.value = "success"
                false -> completeSignUp.value = "fail"
            }
        }
    }

    fun doLogIn(email: String, pw: String) {
        repository.doLogIn(email, pw).let {
            when(it) {
                true -> completeLogIn.value = "success"
                false -> completeLogIn.value = "fail"
            }
        }
    }
}