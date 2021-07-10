package com.example.postexample.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postexample.ui.view.dialog.LoadingProgressDialog
import com.example.postexample.ui.viewmodel.LogInViewModel
import com.example.postexample.ui.viewmodel.PostViewModel

open class BaseFragment: Fragment() {
    open val postViewModel: PostViewModel by activityViewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    PostViewModel(requireActivity().application) as T
        }
    }
    open val loginViewModel: LogInViewModel by activityViewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    LogInViewModel(requireActivity().application) as T
        }
    }

    open val loadingProgressDialog by lazy {
        context?.let { LoadingProgressDialog(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    open fun initView() {
        loadingProgressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        postViewModel.isLoading.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                true -> loadingProgressDialog?.show()
                false -> loadingProgressDialog?.dismiss()
            }
        })
    }
}