package com.example.postexample.ui.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postexample.R
import com.example.postexample.databinding.FragmentHomeBinding
import com.example.postexample.base.BaseFragment
import com.example.postexample.ui.view.adapter.PagingAdapter
import com.example.postexample.ui.viewmodel.ResultState
import com.example.postexample.util.ItemDecoration
import com.example.postexample.util.SwipeHelperCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment: BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val pagingAdapter by lazy {
        PagingAdapter(context, { postInfo ->
            val bundle = Bundle()
            bundle.putSerializable("PostInfo", postInfo)
            findNavController().navigate(R.id.action_homeFragment_to_postDetailFragment, bundle)
        }, { position, postInfo ->
            postViewModel.removePost(position, postInfo.title, postInfo.date)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()

        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(200f)
        }

        pagingAdapter.takeIf { !it.hasObservers() }?.let {
            it.setHasStableIds(true)
        }

        with(binding.rvPostList) {
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context)
            adapter = pagingAdapter
            addItemDecoration(ItemDecoration())
            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        with(postViewModel) {
            loadAllPost()
            allPosts.observe(viewLifecycleOwner, Observer {
                pagingAdapter.refresh()
            })
            deleteState.observe(viewLifecycleOwner, Observer { state ->
                when(state) {
                    ResultState.SUCCESS -> {
                        Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    ResultState.FAIL -> {
                        Toast.makeText(context, "삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
//                deleteState.value = ResultState.NONE
            })
        }

        lifecycleScope.launch {
            delay(300)
            postViewModel.pager.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
}