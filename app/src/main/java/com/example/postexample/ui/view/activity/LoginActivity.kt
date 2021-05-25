package com.example.postexample.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.postexample.R
import com.example.postexample.databinding.ActivityLoginBinding
import com.example.postexample.ui.viewmodel.LogInResult
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.util.LoginPreference
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = loginViewModel
        binding.lifecycleOwner = this

        init()

        if(LoginPreference.getAutoLogin()) {
            startMainActivity()
        }
    }

    fun init() {
        loginViewModel.completeLogIn.observe(this, Observer { result ->
            when(result) {
                LogInResult.SUCCESS -> {
                    startMainActivity()
                    toast("${LoginPreference.getUserName()}님 환영합니다!")
                }
                LogInResult.FAIL -> {
                    toast("아이디 또는 비밀번호를 확인해주세요.")
                }
            }
        })

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}