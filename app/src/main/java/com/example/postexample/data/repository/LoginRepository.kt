package com.example.postexample.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.dao.UserInfoDao
import com.example.postexample.data.database.database.AppDatabase
import com.example.postexample.data.database.entity.Post
import com.example.postexample.data.database.entity.User
import com.example.postexample.util.LoginPreference
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginRepository(application: Application): BaseRepository(application) {
    private val userDao: UserInfoDao = appDatabase.userInfoDao()
    private var user = databaseReference.child("User")

    suspend fun getCurrentUser(): User {
        return GlobalScope.async {
            userDao.getCurrentUser(firebaseAuth.currentUser?.email ?: "")
        }.await()
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

                            // Room DB
                            GlobalScope.launch {
                                userDao.insert(User(name, email, pw))
                            }

                            // Firebase RealTime DB
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
                        GlobalScope.launch {
                            LoginPreference.setUserPreference(getCurrentUser().name, email, pw)
                            singleEmitter.onSuccess(task.result)
                        }
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
}