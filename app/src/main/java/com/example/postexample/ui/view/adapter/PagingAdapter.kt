package com.example.postexample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.postexample.R
import com.example.postexample.databinding.ItemHeaderBinding
import com.example.postexample.databinding.LayoutPostListBinding
import com.example.postexample.model.DataModel
import com.example.postexample.model.DataType

class PagingAdapter(
        private val context: Context?,
        var postItemClick: (DataModel.PostInfo) -> Unit,
        var deleteItemClick: (Int, DataModel.PostInfo) -> Unit
): PagingDataAdapter<DataModel, RecyclerView.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<DataModel>() {
            override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return if(oldItem is DataModel.Header && newItem is DataModel.Header) {
                    oldItem.title == newItem.title
                } else if(oldItem is DataModel.PostInfo && newItem is DataModel.PostInfo) {
                    oldItem.title == newItem.title
                } else{
                    oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {
        DataType.ITEM.ordinal -> PagingItemViewHolder(parent)
        DataType.HEADER.ordinal -> PagingHeaderViewHolder(parent)
        else -> PagingItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PagingItemViewHolder -> holder.bind(getItem(position) as DataModel.PostInfo)
            is PagingHeaderViewHolder -> holder.bind(getItem(position) as DataModel.Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.type?.ordinal ?: DataType.ITEM.ordinal
    }

    inner class PagingHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
    ) {
        private val binding = ItemHeaderBinding.bind(itemView)

        fun bind(data: DataModel.Header) {
            binding.header = data
            binding.executePendingBindings()
        }
    }

    inner class PagingItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list, parent, false)
    ) {
        private val binding = LayoutPostListBinding.bind(itemView)

        fun bind(info: DataModel.PostInfo) {
            binding.postinfo = info
            binding.executePendingBindings()

            binding.swipeView.setOnClickListener {
                postItemClick(info)
            }

            binding.deleteView.setOnClickListener {
                deleteItemClick(position, info)
            }
            binding.invalidateAll()
        }
    }

//    fun addPostInfo(info: PostInfo) {
//        if(!postList.contains(info)) {
//            postList.add(info)
//        }
//        Collections.sort(postList, kotlin.Comparator<PostInfo> { postInfo1: PostInfo, postInfo2: PostInfo ->
//            postInfo2.date?.compareTo(postInfo1.date ?: "") ?: 0
//        })
//        notifyDataSetChanged()
//    }
//
//    fun remove(position: Int) {
//        if (position < postList.size) {
//            postList.removeAt(position)
//        }
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//        postList.clear()
//        notifyDataSetChanged()
//    }
}