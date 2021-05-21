package com.example.postexample.ui.view.adapter.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.postexample.R

object ImageViewBindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(ivThumnail: ImageView, url: String) {
        Glide.with(ivThumnail.context)
                .load(url)
                .error(R.drawable.baseline_warning_24)
                .centerCrop()
                .into(ivThumnail)
    }
}