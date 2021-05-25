package com.example.postexample.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.dao.UserInfoDao
import com.example.postexample.data.database.database.AppDatabase
import com.example.postexample.data.database.entity.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

open class BaseRepository(application: Application){
    open val firebaseAuth = FirebaseAuth.getInstance()
    open val databaseReference =  FirebaseDatabase.getInstance().reference
    open val appDatabase = AppDatabase.getInstance(application)!!
}