package com.example.postexample.ui.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.postexample.R
import com.example.postexample.databinding.FragmentPostAddBinding
import com.example.postexample.ui.base.BaseFragment
import com.example.postexample.ui.view.dialog.LoadingProgressDialog
import com.example.postexample.ui.viewmodel.ResultState

class PostAddFragment: BaseFragment() {
    private lateinit var binding: FragmentPostAddBinding
    private val loadingProgressDialog by lazy {
        context?.let { LoadingProgressDialog(it) }
    }

    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        postViewModel.clickImage.value = false
        with(result) {
            takeIf { resultCode == Activity.RESULT_OK }?.let {
                data?.data.run {
                    context?.let { context ->
                        Glide.with(context)
                            .load(this)
                            .error(R.drawable.baseline_warning_24)
                            .into(binding.ivPostImg)
                    }

                    postViewModel.uri.value = this.toString()
                }
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
        return binding.root
    }

    override fun initView() {
        loadingProgressDialog?.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        with(postViewModel) {
            clickImage.value = false
            clickImage.observe(viewLifecycleOwner, Observer { result ->
                result.takeIf { it == true }?.let {
                    var intent = Intent(Intent.ACTION_PICK).apply {
                        type = MediaStore.Images.Media.CONTENT_TYPE
                        type = "image/*"
                    }
                    startActivityResult.launch(intent)
                }
            })

            uploadState.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    ResultState.SUCCESS -> Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show()
                    ResultState.FAIL -> Toast.makeText(context, "업로드 실패", Toast.LENGTH_SHORT).show()
                }
            })

            isLoading.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    true -> loadingProgressDialog?.show()
                    false -> loadingProgressDialog?.dismiss()
                }
            })
        }
    }
}