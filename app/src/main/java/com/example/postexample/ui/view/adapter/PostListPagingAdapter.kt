package com.example.postexample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.postexample.R
import com.example.postexample.data.database.entity.Post
import com.example.postexample.databinding.LayoutPostListBinding

class PostListPagingAdapter(
        private val context: Context?,
        var postItemClick: (Post) -> Unit,
        var deleteItemClick: (Int, Post) -> Unit
): PagingDataAdapter<Post, PostListPagingAdapter.PagingItemViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return if(oldItem is Post && newItem is Post) {
                    oldItem.title == newItem.title
                } else{
                    oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListPagingAdapter.PagingItemViewHolder = PagingItemViewHolder(parent)
//        DataType.ITEM.ordinal -> PagingItemViewHolder(parent)
//        DataType.HEADER.ordinal -> PagingHeaderViewHolder(parent)

    override fun onBindViewHolder(holder: PostListPagingAdapter.PagingItemViewHolder, position: Int) {
        when(holder) {
            is PagingItemViewHolder -> holder.bind(getItem(position) as Post)
//            is PagingHeaderViewHolder -> holder.bind(getItem(position) as DataModel.Header)
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return getItem(position)?.type?.ordinal ?: DataType.ITEM.ordinal
//    }

//    inner class PagingHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
//    ) {
//        private val binding = ItemHeaderBinding.bind(itemView)
//
//        fun bind(data: DataModel.Header) {
//            binding.header = data
//            binding.executePendingBindings()
//        }
//    }

    inner class PagingItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list, parent, false)
    ) {
        private val binding = LayoutPostListBinding.bind(itemView)

        fun bind(info: Post) {
            binding.postinfo = info
            binding.executePendingBindings()

            binding.swipeView.setOnClickListener {
                postItemClick(info)
            }

            binding.deleteView.setOnClickListener {
                deleteItemClick(position, info)
            }

            binding.btnLike.setOnClickListener {
                when(it.tag) {
                    "like" -> {
                        it.tag = "unlike"
                        it.setBackgroundResource(R.drawable.img_unlike)
                        info.likenum = (info.likenum.toInt() - 1).toString()
                    }
                    "unlike" -> {
                        it.tag = "like"
                        it.setBackgroundResource(R.drawable.img_like)
                        info.likenum = (info.likenum.toInt() + 1).toString()
                    }
                }
                binding.invalidateAll()
            }
        }
    }
}