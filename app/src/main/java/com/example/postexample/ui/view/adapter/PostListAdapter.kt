package com.example.postexample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postexample.databinding.LayoutPostListBinding
import com.example.postexample.model.PostInfo
import java.util.*

class PostListAdapter(
        private val context: Context?,
        var postItemClick: (PostInfo) -> Unit,
        var deleteItemClick: (Int, PostInfo) -> Unit
) : RecyclerView.Adapter<PostListAdapter.PostListHolder>() {
    private var postList: ArrayList<PostInfo> = arrayListOf()

    inner class PostListHolder(private val binding: LayoutPostListBinding) : RecyclerView.ViewHolder(
            binding.root
    ) {
        fun bind(info: PostInfo, position: Int) {
            binding.postinfo = info

            binding.swipeView.setOnClickListener {
                postItemClick(info)
            }

            binding.deleteView.setOnClickListener {
                deleteItemClick(position, info)
            }
            binding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListHolder
            = PostListHolder(
            LayoutPostListBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: PostListHolder, position: Int) {
        holder.bind(postList[position], position)
    }

    override fun getItemCount(): Int = postList.size

    override fun getItemId(position: Int): Long = postList.get(position).toString().hashCode().toLong()

    fun addPostInfo(info: PostInfo) {
        if(!postList.contains(info)) {
            postList.add(info)
        }
        Collections.sort(postList, kotlin.Comparator<PostInfo> { postInfo1: PostInfo, postInfo2: PostInfo ->
            postInfo2.date?.compareTo(postInfo1.date ?: "") ?: 0
        })
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position < postList.size) {
            postList.removeAt(position)
        }
        notifyDataSetChanged()
    }

    fun clear() {
        postList.clear()
        notifyDataSetChanged()
    }
}