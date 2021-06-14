package com.example.postexample.data.repository

import android.app.Application
import android.util.Log
import com.example.postexample.data.database.dao.UserInfoDao
import com.example.postexample.data.database.entity.User
import com.example.postexample.util.LoginPreference
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class LoginRepository(application: Application): BaseRepository(application) {
    private val userDao: UserInfoDao = appDatabase.userInfoDao()
    private var user = databaseReference.child("User")

    suspend fun getCurrentUser(email: String): User {
        return GlobalScope.async {
            userDao.getCurrentUser(email)
        }.await()
    }

    fun loadAllUser(): Single<DataSnapshot> =
        Single.create { singleEmitter ->
            user.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    singleEmitter.onSuccess(snapshot)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

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

                            // Firebase RealTime DB
                            databaseReference
                                    .child("User")
                                    .push()
                                    .setValue(userMap)

                            singleEmitter.onSuccess(result!!)
                        } ?: run {
                            Log.e("seolim", "" + exception)
                            singleEmitter.onError(exception!!)
                        }
                    }
                }
        }

    fun doLogIn(email: String, pw: String): Single<String> =
        Single.create { singleEmitter ->
            firebaseAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener { task ->
                    task.takeIf { it.isSuccessful() }?.let {
                        singleEmitter.onSuccess(email)
                    } ?: run {
                        Log.e("seolim", "" + task.exception)
                        singleEmitter.onError(task.exception!!)
                    }
                }
        }

    fun doLogOut() {
        firebaseAuth.signOut()
        LoginPreference.setAutoLogin(false)
    }

    suspend fun insertUser(name: String, email: String, pw: String) {
        userDao.insert(User(name, email, pw))
    }

    suspend fun deleteUser(name: String, email: String, pw: String) {
        userDao.delete(User(name, email, pw))
    }
}