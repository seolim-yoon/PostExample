package com.example.postexample.data.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.entity.Post
import com.example.postexample.util.LoginPreference
import com.example.postexample.util.TimeFormatUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class PostRepository(application: Application): BaseRepository(application) {
    private val postDao: PostInfoDao = appDatabase.postInfoDao()
    private val posts: LiveData<List<Post>> = postDao.getAllPost()
    private var post = databaseReference.child("Post")

    private var uidList = arrayListOf<String>()

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
                                                "content" to content,
                                                "name" to LoginPreference.getUserName(),
                                                "date" to TimeFormatUtils.dateFormat.format(Date(System.currentTimeMillis()))
                                        )

                                        // Firebase RealTime DB
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

    fun removePost(position: Int, title: String): Single<Int> =
            Single.create { singleEmitter->
                FirebaseStorage.getInstance()
                        .getReferenceFromUrl("gs://postexamp.appspot.com")
                        .child("post/")
                        .child("img_${title}.jpg")
                        .run {
                            delete()
                                    .addOnSuccessListener { task ->

                                        databaseReference
                                                .child("Content")
                                                .child(uidList.get(position))
                                                .removeValue()

                                        singleEmitter.onSuccess(position)
                                    }
                                    .addOnFailureListener { exception ->
                                        singleEmitter.onError(exception)
                                    }
                        }
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
                    uidList.clear()
                    snapshot.children.forEach {
                        uidList.add(it.key ?: "")
                    }
                    singleEmitter.onSuccess(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("seolim", "error : " + error.toException())
                    singleEmitter.onError(error.toException())
                }
            })
        }

    suspend fun insertPost(url: String, title: String, content: String, name: String, date: String) {
        postDao.insert(Post(url, title, content, name, date))
    }

    suspend fun deletePost(url: String) {
        postDao.deletePostByURL(url)
    }
}