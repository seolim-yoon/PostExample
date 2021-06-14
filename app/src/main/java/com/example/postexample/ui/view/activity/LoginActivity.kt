package com.example.postexample.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postexample.R
import com.example.postexample.databinding.ActivityLoginBinding
import com.example.postexample.ui.viewmodel.LogInResult
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.util.LoginPreference
import com.example.postexample.util.PermissionCheck
import org.jetbrains.anko.toast

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LogInViewModel by viewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    LogInViewModel(application) as T
        }
    }

    private val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val permissionCheck = PermissionCheck(this, permissions)
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] == false || permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == false) {
            finish()
        } else {
            init()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = loginViewModel
        binding.lifecycleOwner = this

        when(permissionCheck.checkPermission()) {
            true -> init()
            false -> permissionLauncher.launch(permissions)
        }
    }

    private fun init() {
        if(LoginPreference.getAutoLogin()) {
            startMainActivity()
        }

        with(loginViewModel) {
            loadAllUser()
            completeLogIn.observe(this@LoginActivity, Observer { result ->
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
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}