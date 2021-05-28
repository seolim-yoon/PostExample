package com.example.postexample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postexample.databinding.LayoutPostListBinding
import com.example.postexample.model.PostInfo
import java.util.*
import kotlin.collections.ArrayList

class PostListAdapter(
    private val context: Context?,
    var postItemClick: (PostInfo) -> Unit
) : RecyclerView.Adapter<PostListAdapter.PostListHolder>() {
    private var postList: ArrayList<PostInfo> = arrayListOf()

    inner class PostListHolder(private val binding: LayoutPostListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(info: PostInfo) {
            binding.postinfo = info

            itemView.setOnClickListener {
                postItemClick(info)
                binding.invalidateAll()
            }
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
        holder.bind(postList[position])
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

    fun clear() {
        postList.clear()
        notifyDataSetChanged()
    }
}