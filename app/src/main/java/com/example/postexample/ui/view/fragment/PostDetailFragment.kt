package com.example.postexample.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.postexample.R
import com.example.postexample.base.BaseFragment
import com.example.postexample.data.database.entity.Post
import com.example.postexample.databinding.FragmentDetailBinding

class PostDetailFragment: BaseFragment() {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initView() {
        binding.postinfo = arguments?.getSerializable("PostInfo") as Post

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}