package com.example.postexample.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DataBaseRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference =  FirebaseDatabase.getInstance().getReference()

    fun doSignUp(name: String, email: String, pw: String): Boolean {
        var result = false
        firebaseAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener {
                if(it.isSuccessful()) {
                    var userMap = hashMapOf<String, String>().apply{
                        put("name", name)
                        put("email", email)
                        put("pw", pw)
                    }

                    databaseReference.child(firebaseAuth.currentUser?.uid ?: "").setValue(userMap)
                    result = true
                } else {
                    Log.e("seolim", "" + it.exception)
                    result = false
                }
            }
        return result
    }

    fun doLogIn(email: String, pw: String): Boolean {
        var result = false
        firebaseAuth.signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener {
                if(it.isSuccessful()) {
                    result = true
                } else{
                    result = false
                }
            }
        return result
    }
}