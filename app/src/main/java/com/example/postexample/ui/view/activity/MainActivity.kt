package com.example.postexample.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.postexample.R
import com.example.postexample.databinding.ActivityMainBinding
import com.example.postexample.ui.view.fragment.PostAddFragment
import com.example.postexample.ui.view.fragment.HomeFragment
import com.example.postexample.ui.view.fragment.UserInfoFragment
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.ui.viewmodel.PostViewModel
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private val TIME_INTERVAL = 2000
    private var backKeyPressedTime = 0L
    private val homeFragment by lazy { HomeFragment() }
    private val postAddFragment by lazy { PostAddFragment() }
    private val userInfoFragment by lazy { UserInfoFragment() }

    private val postViewModel: PostViewModel by viewModels()
    private val loginViewModel: LogInViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    fun init() {
        setSupportActionBar(binding.tbAppTitle)

        binding.bnMenu.run {
            setOnNavigationItemSelectedListener { menuitem ->
                when(menuitem.itemId) {
                    R.id.item_home -> replaceFragment(homeFragment)
                    R.id.item_postadd -> replaceFragment(postAddFragment)
                    R.id.item_info -> replaceFragment(userInfoFragment)
                }
                true
            }
            selectedItemId = R.id.item_home
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + TIME_INTERVAL) {
            backKeyPressedTime = System.currentTimeMillis()
            toast("뒤로 버튼을 한번 더 누르시면 종료됩니다.")
        } else {
            finish()
        }
    }
}