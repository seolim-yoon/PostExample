package com.example.postexample.base

import android.app.Application
import com.example.postexample.data.database.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

open class BaseRepository(application: Application){
    open val firebaseAuth = FirebaseAuth.getInstance()
    open val databaseReference = FirebaseDatabase.getInstance().reference
    open val appDatabase = AppDatabase.getInstance(application)!!
}