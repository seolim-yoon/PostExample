package com.example.postexample.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postexample.R
import com.example.postexample.databinding.FragmentPostAddBinding
import com.example.postexample.viewmodel.LogInViewModel

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