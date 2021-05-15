package com.example.postexample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postexample.databinding.LayoutPostListBinding
import com.example.postexample.model.PostInfo

class PostListAdapter(private val context: Context?,
                      var postItemClick: (PostInfo) -> Unit
) : RecyclerView.Adapter<PostListAdapter.VideoListHolder>() {
    private var postInfo: List<PostInfo> = listOf()

    inner class VideoListHolder(private val binding: LayoutPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: PostInfo) {
            binding.postinfo = info

            itemView.setOnClickListener {
                postItemClick(info)
                binding.invalidateAll()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListHolder
            = VideoListHolder(LayoutPostListBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: VideoListHolder, position: Int) {
        holder.bind(postInfo[position])
    }

    override fun getItemCount(): Int = postInfo.size

    fun setPostInfo(postinfo: List<PostInfo>) {
        this.postInfo = postinfo;
        notifyDataSetChanged()
    }
}