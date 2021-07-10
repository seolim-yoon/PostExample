package com.example.postexample.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.postexample.R
import com.example.postexample.databinding.ActivityMainBinding
import com.example.postexample.ui.view.fragment.PostAddFragment
import com.example.postexample.ui.view.fragment.HomeFragment
import com.example.postexample.ui.view.fragment.UserInfoFragment
import com.example.postexample.ui.viewmodel.PostViewModel
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TIME_INTERVAL = 2000
    private var backKeyPressedTime = 0L
    private val homeFragment by lazy { HomeFragment() }
    private val postAddFragment by lazy { PostAddFragment() }
    private val userInfoFragment by lazy { UserInfoFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init() {
        binding.bnMenu.setupWithNavController(findNavController(R.id.main_nav_host))
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + TIME_INTERVAL) {
            backKeyPressedTime = System.currentTimeMillis()
            toast("뒤로 버튼을 한번 더 누르시면 종료됩니다.")
        } else {
            finish()
        }
    }
}