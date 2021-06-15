package com.example.postexample.data.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.entity.Post
import com.example.postexample.util.LoginPreference
import com.example.postexample.util.TimeFormatUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.collections.HashMap

class PostRepository(application: Application): BaseRepository(application) {
    private val postDao: PostInfoDao = appDatabase.postInfoDao()
    private var post = databaseReference.child("Post")

    private var uidList = arrayListOf<String>()

    fun getAllPost(): Single<List<Post>> {
        return postDao.getAllPost()
    }

    fun loadAllPost(): Single<DataSnapshot> =
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
                }
            })
        }

    fun createPost(uri: String, title: String, content: String): Single<UploadTask.TaskSnapshot> =
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
                                                "date" to TimeFormatUtils.dateFormat.format(Date(System.currentTimeMillis())),
                                                "likenum" to "0"
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
            Single.create { singleEmitter ->
                FirebaseStorage.getInstance()
                        .getReferenceFromUrl("gs://postexamp.appspot.com")
                        .child("post/")
                        .child("img_${title}.jpg")
                        .run {
                            delete()
                                    .addOnSuccessListener { task ->
                                        databaseReference
                                                .child("Post")
                                                .child(uidList.get(position))
                                                .removeValue()

                                        singleEmitter.onSuccess(position)
                                    }
                                    .addOnFailureListener { exception ->
                                        singleEmitter.onError(exception)
                                    }
                        }
            }

    fun loadImageURL(title: String): Single<Uri> =
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


    fun changePost(): Observable<HashMap<String, DataSnapshot>> =
            Observable.create({ singleEmitter ->
                post.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.e("seolim", "onChildAdded")
                        singleEmitter.onNext(hashMapOf("add" to snapshot))
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.e("seolim", "onChildChanged")
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        Log.e("seolim", "onChildRemoved")
                        singleEmitter.onNext(hashMapOf("remove" to snapshot))
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            })


    fun likeUnlikePost() : Single<UploadTask.TaskSnapshot> =
        Single.create { singleEmitter ->
            databaseReference
                .child("Post")
//            singleEmitter.onSuccess(task)
        }

    suspend fun insertPost(post: Post) {
        postDao.insert(post)
    }

    suspend fun updatePost(post: Post) {
        postDao.update(post)
    }

    suspend fun deletePost(date: String) {
        postDao.deletePostByDate(date)
    }

    suspend fun deleteAll() {
        postDao.deleteAll()
    }
}