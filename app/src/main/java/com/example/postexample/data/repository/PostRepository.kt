package com.example.postexample.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Single

class PostRepository: BaseRepository() {
    var post = databaseReference.child("Post")

    fun createPost(url: String, title: String, content: String) {
        var postMap = hashMapOf(
                "url" to url,
                "title" to title,
                "content" to content
        )

        databaseReference
                .child("Post")
                .push()
                .setValue(postMap)
    }

    fun deletePost(url: String, title: String, content: String) {

    }

    fun refreshPost() : Single<DataSnapshot> =
        Single.create { singleEmitter ->
            post.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    singleEmitter.onSuccess(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    singleEmitter.onError(error.toException())
                }
            })
        }
}