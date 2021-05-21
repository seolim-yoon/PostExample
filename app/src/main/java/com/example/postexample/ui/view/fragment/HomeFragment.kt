package com.example.postexample.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.postexample.R
import com.example.postexample.databinding.FragmentHomeBinding
import com.example.postexample.model.UserInfo
import com.example.postexample.ui.base.BaseFragment
import com.example.postexample.ui.view.adapter.PostListAdapter

class HomeFragment: BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val postListAdapter by lazy {
        PostListAdapter(context) {
            // TODO : post list click listener

        }
    }

    lateinit var userInfo: UserInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun initView() {
        binding.rvPostList.layoutManager = GridLayoutManager(context, 1)
        binding.rvPostList.adapter = postListAdapter

        loginViewModel.getUserInfo()
        postViewModel.refreshPost()

        postViewModel.postInfo.observe(viewLifecycleOwner, Observer { postinfo ->
            postinfo.name = userInfo.name ?: ""
            postListAdapter.addPostInfo(postinfo)
        })

        loginViewModel.userInfo.observe(viewLifecycleOwner, Observer { userinfo ->
            this.userInfo = userinfo
        })
    }
}