package com.example.postexample.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.postexample.R
import com.example.postexample.databinding.ActivitySignupBinding
import com.example.postexample.viewmodel.LogInViewModel
import com.example.postexample.viewmodel.LoginResult
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val loginViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewmodel = loginViewModel

        init()
    }

    private fun init() {
        loginViewModel.completeSignUp.observe(this, {
            when (it) {
                LoginResult.SUCCESS -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    toast("회원가입이 완료 되었습니다!")
                }
                LoginResult.FAIL -> {
                    toast("회원가입 실패 하였습니다.")
                }
            }
        })
    }
}