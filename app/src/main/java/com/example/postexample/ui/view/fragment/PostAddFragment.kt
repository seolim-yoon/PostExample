package com.example.postexample.ui.view.fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.postexample.R
import com.example.postexample.databinding.FragmentPostAddBinding
import com.example.postexample.ui.view.adapter.PostListAdapter
import com.example.postexample.ui.viewmodel.PostViewModel
import com.example.postexample.ui.viewmodel.RefreshPost
import com.example.postexample.ui.viewmodel.UploadState

class PostAddFragment: Fragment() {
    private lateinit var binding: FragmentPostAddBinding
    private val postViewModel: PostViewModel by viewModels()
    private val postListAdapter by lazy {
        PostListAdapter(context) {
            // TODO : post list click listener

        }
    }

    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        with(result) {
            takeIf { resultCode == Activity.RESULT_OK }?.let { result ->
                val uri = result.data?.data

                val cursor: Cursor? = activity?.contentResolver?.query(uri ?: "".toUri(), null, null, null, null)
                cursor?.moveToNext()

                val index = cursor!!.getColumnIndex(MediaStore.Images.Media.DATA)
                val source = cursor.getString(index)

                val bitmap = BitmapFactory.decodeFile(source)
                binding.ivPostImg.setImageBitmap(bitmap)

                postViewModel.clickImage.value = false
                postViewModel.url.value = uri.toString()
                Log.v("seolim", "uri : " + uri.toString())
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_add, container, false)
        binding.viewmodel = postViewModel
        initView()
        return binding.root
    }

    fun initView() {
        postViewModel.clickImage.observe(viewLifecycleOwner, Observer { result ->
            result.takeIf { it == true }?.let {
                var intent = Intent(Intent.ACTION_PICK).apply {
                    type = MediaStore.Images.Media.CONTENT_TYPE
                    type = "image/*"
                }
                startActivityResult.launch(intent)
            }
        })

        postViewModel.uploadState.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                UploadState.SUCCESS -> Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show()
                UploadState.FAIL -> Toast.makeText(context, "업로드 실패", Toast.LENGTH_SHORT).show()
            }
        })

        postViewModel.refreshPost.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                RefreshPost.SUCCESS -> {
                    // TODO : 리사이클러뷰


                }
                RefreshPost.FAIL -> {

                }
            }
        })
    }
}