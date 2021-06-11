package com.example.postexample.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.ui.viewmodel.PostViewModel

open class BaseFragment: Fragment() {
    open val postViewModel: PostViewModel by activityViewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    PostViewModel(activity?.application!!) as T
        }
    }
    open val loginViewModel: LogInViewModel by activityViewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    LogInViewModel(activity?.application!!) as T
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    open fun initView() {

    }
}