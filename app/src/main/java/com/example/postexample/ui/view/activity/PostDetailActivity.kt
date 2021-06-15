package com.example.postexample.ui.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.postexample.R
import com.example.postexample.databinding.ActivityPostDetailBinding
import com.example.postexample.model.PostInfo

class PostDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        init()
    }

    private fun init() {
        binding.postinfo = PostInfo(intent.getStringExtra("url"),
                intent.getStringExtra("title"),
                intent.getStringExtra("content"),
                intent.getStringExtra("name"),
                intent.getStringExtra("date"))

        setSupportActionBar(binding.tbPostDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}