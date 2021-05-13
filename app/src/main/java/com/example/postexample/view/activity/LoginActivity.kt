package com.example.postexample.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.postexample.R
import com.example.postexample.databinding.ActivityLoginBinding
import com.example.postexample.viewmodel.LogInViewModel
import com.example.postexample.viewmodel.LoginResult
import org.jetbrains.anko.toast

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = loginViewModel

        init()
    }

    private fun init() {
        loginViewModel.completeLogIn.observe(this, {
            when(it) {
                LoginResult.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    toast("환영합니다!")
                }
                LoginResult.FAIL -> {
                    toast("아이디 또는 비밀번호를 확인해주세요.")
                }
            }
        })

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}