package com.example.postexample.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.ui.viewmodel.PostViewModel

open class BaseFragment: Fragment() {
    open val postViewModel: PostViewModel by activityViewModels()
    open val loginViewModel: LogInViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    open fun initView() {

    }
}