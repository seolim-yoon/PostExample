package com.example.postexample.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.postexample.R
import com.example.postexample.databinding.ActivitySignupBinding
import com.example.postexample.ui.viewmodel.LogInResult
import com.example.postexample.ui.viewmodel.LogInViewModel
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val loginViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewmodel = loginViewModel
        binding.lifecycleOwner = this

        init()
    }

    fun init() {
        loginViewModel.completeSignUp.observe(this, Observer { result ->
            when (result) {
                LogInResult.SUCCESS -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    toast("회원가입이 완료 되었습니다!")
                }
                LogInResult.FAIL -> {
                    toast("회원가입 실패 하였습니다.")
                }
            }
        })
    }
}