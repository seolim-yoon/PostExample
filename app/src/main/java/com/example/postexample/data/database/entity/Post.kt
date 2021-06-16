package com.example.postexample.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(var url: String,
                var title: String,
                var content: String,
                var name: String,
                var date: String,
                var likenum: String) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}