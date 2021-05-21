package com.example.postexample.data.repository

import android.util.Log
import com.example.postexample.util.preference.LoginPreference
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Single

class LoginRepository: BaseRepository() {
    var user = databaseReference.child("User")

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

                            databaseReference
                                    .child("User")
                                    .push()
                                    .setValue(userMap)
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
                        LoginPreference.setUserPreference(email, pw)
                        singleEmitter.onSuccess(task.result)
                    } ?: run {
                        Log.e("seolim", "" + task.exception)
                        singleEmitter.onError(task.exception)
                    }
                }
        }

    fun doLogOut() {
        firebaseAuth.signOut()
        LoginPreference.setAutoLogin(false)
    }

    fun getUserInfo() : Single<DataSnapshot> =
            Single.create { singleEmitter ->
                user.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        singleEmitter.onSuccess(snapshot)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        singleEmitter.onError(error.toException())
                    }
                })
            }
}