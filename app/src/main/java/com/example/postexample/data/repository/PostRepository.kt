package com.example.postexample.data.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.database.AppDatabase
import com.example.postexample.data.database.entity.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.reactivex.rxjava3.core.Single

class PostRepository(application: Application): BaseRepository() {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val postDao: PostInfoDao = appDatabase.postInfoDao()
    private val posts: LiveData<List<Post>> = postDao.getAllPost()

    var post = databaseReference.child("Post")

    fun getAllPost(): LiveData<List<Post>> {
        return posts
    }

    fun createPost(uri: String, title: String, content: String) : Single<UploadTask.TaskSnapshot>  =
            Single.create { singleEmitter ->
                FirebaseStorage.getInstance()
                        .getReferenceFromUrl("gs://postexamp.appspot.com")
                        .child("post/")
                        .child("img_${title}.jpg")
                        .run {
                            putFile(Uri.parse(uri))
                                    .addOnSuccessListener { task ->
                                        var postMap = hashMapOf(
                                                "url" to downloadUrl.toString(),
                                                "title" to title,
                                                "content" to content
                                        )

                                        databaseReference
                                                .child("Post")
                                                .push()
                                                .setValue(postMap)

                                        singleEmitter.onSuccess(task)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("seolim", "exception : " + exception.message)
                                        singleEmitter.onError(exception)
                                    }
                        }
        }

    fun deletePost(url: String, title: String, content: String) {
        postDao.delete(Post(url, title, content))
    }

    fun loadImageURL(title: String) : Single<Uri> =
        Single.create { singleEmitter ->
            FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://postexamp.appspot.com")
                .child("post/img_${title}.jpg")
                .downloadUrl
                .addOnSuccessListener { uri ->
                    singleEmitter.onSuccess(uri)

                }.addOnFailureListener { error ->
                    singleEmitter.onError(error)
                }
        }

    fun refreshPost() : Single<DataSnapshot> =
        Single.create { singleEmitter ->
            post.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    singleEmitter.onSuccess(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("seolim", "error : " + error.toException())
                    singleEmitter.onError(error.toException())
                }
            })
        }
}