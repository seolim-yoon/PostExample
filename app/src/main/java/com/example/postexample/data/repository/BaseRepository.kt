package com.example.postexample.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

open class BaseRepository {
    open val firebaseAuth = FirebaseAuth.getInstance()
    open val databaseReference =  FirebaseDatabase.getInstance().reference
}