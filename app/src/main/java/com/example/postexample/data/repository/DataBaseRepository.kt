package com.example.postexample.data.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.core.Single
import kotlin.math.sin

class DataBaseRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference =  FirebaseDatabase.getInstance().reference

    fun doSignUp(name: String, email: String, pw: String): Single<AuthResult> =
        Single.create { singleEmitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener { task ->
                    with(task) {
                        takeIf { isSuccessful }?.let {
                            var userMap = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "pw" to pw
                            )

                            databaseReference.child(firebaseAuth.currentUser?.uid ?: "").setValue(userMap)
                            singleEmitter.onSuccess(result)
                        } ?: run {
                            Log.e("seolim", "" + exception)
                            singleEmitter.onError(exception)
                        }
                    }
                }
        }

    fun doLogIn(email: String, pw: String): Single<AuthResult> =
        Single.create { singleEmitter ->
            firebaseAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener { task ->
                    task.takeIf { it.isSuccessful() }?.let {
                        singleEmitter.onSuccess(task.result)
                    } ?: run {
                        Log.e("seolim", "" + task.exception)
                        singleEmitter.onError(task.exception)
                    }
                }
        }
}