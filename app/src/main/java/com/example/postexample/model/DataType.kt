package com.example.postexample.model

import java.io.Serializable

enum class DataType {
    HEADER, ITEM
}

sealed class DataModel(val type: DataType) : Serializable{
    data class PostInfo(val url: String, val title: String, val content: String, val name: String, val date: String, var likenum: String): DataModel(DataType.ITEM)
    data class Header(val title: String): DataModel(DataType.HEADER)
}