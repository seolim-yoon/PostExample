package com.example.postexample.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.postexample.R
import com.example.postexample.databinding.ActivityMainBinding
import com.example.postexample.ui.view.fragment.PostAddFragment
import com.example.postexample.ui.view.fragment.HomdFragment
import com.example.postexample.ui.view.fragment.UserInfoFragment
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private val TIME_INTERVAL = 2000
    private var backKeyPressedTime = 0L
    private val homeFragment by lazy { HomdFragment() }
    private val postAddFragment by lazy { PostAddFragment() }
    private val userInfoFragment by lazy { UserInfoFragment() }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initNavigationBar()
    }

    fun initNavigationBar() {
        binding.bnMenu.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
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