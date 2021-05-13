package com.example.postexample.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.postexample.R
import com.example.postexample.databinding.FragmentPostAddBinding

class PostAddFragment: Fragment() {
    private lateinit var binding: FragmentPostAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_add, container, false)
        initView()
        return binding.root
    }

    fun initView() {

    }
}