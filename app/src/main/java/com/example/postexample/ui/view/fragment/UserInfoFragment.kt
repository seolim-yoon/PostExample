package com.example.postexample.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.postexample.R
import com.example.postexample.databinding.FragmentUserInfoBinding
import com.example.postexample.model.UserInfo
import com.example.postexample.ui.base.BaseFragment
import com.example.postexample.ui.view.activity.LoginActivity
import com.example.postexample.ui.viewmodel.UserState
import com.example.postexample.util.LoginPreference

class UserInfoFragment: BaseFragment() {
    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)
        binding.viewmodel = loginViewModel
        binding.userinfo = UserInfo(LoginPreference.getUserName(), LoginPreference.getUserEmail())
        return binding.root
    }

    override fun initView() {
        loginViewModel.userState.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                UserState.LOGOUT -> {
                    startActivity(Intent(context, LoginActivity::class.java))
                    requireActivity().finish()
                }
                UserState.LOGIN -> {
                    // TODO : 로그인 된 상태 = 사용자 정보 띄우기
                }
            }
        })
    }
}