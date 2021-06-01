package com.example.postexample.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postexample.R
import com.example.postexample.databinding.FragmentHomeBinding
import com.example.postexample.ui.base.BaseFragment
import com.example.postexample.ui.view.activity.PostDetailActivity
import com.example.postexample.ui.view.adapter.PostListAdapter
import com.example.postexample.ui.viewmodel.ResultState
import com.example.postexample.util.ItemDecoration
import com.example.postexample.util.SwipeHelperCallback

class HomeFragment: BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val postListAdapter by lazy {
        PostListAdapter(context, { postInfo ->
            activity?.let {
                val intent = Intent(context, PostDetailActivity::class.java).apply {
                    putExtra("url", postInfo.uri)
                    putExtra("title", postInfo.title)
                    putExtra("content", postInfo.content)
                    putExtra("name", postInfo.name)
                    putExtra("date", postInfo.date)
                }
                startActivity(intent)
            }
        }, { position, postInfo ->
            postViewModel.removePost(position, postInfo.title ?: "", postInfo.uri ?: "")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun initView() {
        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(200f)
        }

        postListAdapter.takeIf { !it.hasObservers() }?.let {
            it.setHasStableIds(true)
        }

        with(binding.rvPostList) {
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context)
            adapter = postListAdapter
            addItemDecoration(ItemDecoration())
            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        with(postViewModel) {
            refreshPost()
            postInfo.observe(viewLifecycleOwner, Observer { postinfo ->
                postListAdapter.addPostInfo(postinfo)
            })
            deletePosition.observe(viewLifecycleOwner, Observer { position ->
                postListAdapter.remove(position)
                Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
            })
        }
    }
}