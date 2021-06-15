package com.example.postexample.ui.view.adapter.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postexample.R
import com.example.postexample.model.DataModel
import com.example.postexample.model.DataType
import com.example.postexample.ui.view.adapter.PagingAdapter

object RecyclerviewBindingAdapter {
    @BindingAdapter("bindData")
    @JvmStatic
    fun bindData(recyclerView: RecyclerView, data: List<DataModel.PostInfo>) {
        val adapter = recyclerView.adapter as? PagingAdapter
        adapter?.let {
            it.notifyDataSetChanged()
        }
    }
}