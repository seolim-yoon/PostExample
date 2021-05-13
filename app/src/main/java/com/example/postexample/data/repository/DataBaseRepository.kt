package com.example.postexample.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.core.Single

class DataBaseRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference

    fun doSignUp(name: String, email: String, pw: String) =
        Single.just(firebaseAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener {
                it.takeIf { it.isSuccessful && it.result != null }?.let {
                    val userMap = {
                        "name" to name
                        "email" to email
                        "pw" to pw
                    }
                    databaseReference.child(firebaseAuth.currentUser?.uid ?: "").setValue(userMap)
                } ?: run {
                    Log.e("seolim", "" + it.exception)
                }
            })

    fun doLogIn(email: String, pw: String) = Single.just(firebaseAuth.signInWithEmailAndPassword(email, pw))
}