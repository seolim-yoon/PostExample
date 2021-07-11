package com.example.postexample.data.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.postexample.base.BaseRepository
import com.example.postexample.data.database.dao.PostInfoDao
import com.example.postexample.data.database.entity.Post
import com.example.postexample.util.LoginPreference
import com.example.postexample.util.TimeFormatUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import kotlin.collections.HashMap

class PostRepository(application: Application) : BaseRepository(application) {
    private val postDao: PostInfoDao = appDatabase.postInfoDao()
    private val post = databaseReference.child("Post")
    private val firebaseStorageRef = FirebaseStorage.getInstance()
            .getReferenceFromUrl("gs://postexamp.appspot.com")
            .child("post/")

    private var uidList = arrayListOf<String>()

    fun getPagingData(): Single<List<Post>> = postDao.getAllPost()

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

    fun createPost(uri: String, title: String, content: String): Single<HashMap<String, String>> =
            Single.create { singleEmitter ->
                // 우선 storage에 먼저 이미지를 저장한 후
                firebaseStorageRef
                        .child("img_${title}.jpg")
                        .run {
                            putFile(Uri.parse(uri))
                                    .addOnSuccessListener { task ->
                                        // 이미지 저장 성공 시 저장된 이미지의 url을 가져옴
                                        downloadUrl.addOnSuccessListener { url ->
                                            var postMap = hashMapOf(
                                                    "url" to url.toString(),
                                                    "title" to title,
                                                    "content" to content,
                                                    "name" to LoginPreference.getUserName(),
                                                    "date" to TimeFormatUtils.dateFormat.format(Date(System.currentTimeMillis())),
                                                    "likenum" to "0"
                                            )

                                            // Firebase RealTime DB에 저장
                                            databaseReference
                                                    .child("Post")
                                                    .push()
                                                    .setValue(postMap)

                                            singleEmitter.onSuccess(postMap)
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("seolim", "exception : " + exception.message)
                                            singleEmitter.onError(exception)

                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("seolim", "exception : " + exception.message)
                                        singleEmitter.onError(exception)
                                    }
                        }
            }

    fun removePost(position: Int, title: String): Single<Int> =
            Single.create { singleEmitter ->
                firebaseStorageRef
                        .child("img_${title}.jpg")
                        .delete()
                        .addOnSuccessListener { task ->
                            databaseReference
                                    .child("Post")
                                    .child(uidList.get(position))
                                    .removeValue()
                                    .addOnSuccessListener {
                                        singleEmitter.onSuccess(position)
                                    }
                        }
                        .addOnFailureListener { exception ->
                            singleEmitter.onError(exception)
                        }
            }

    fun getPostByDate(date: String) : Single<List<Post>> {
        return postDao.getPostByDate(date)
    }

    fun insertPost(post: Post): Completable = postDao.insert(post)

    fun deletePostByDate(date: String) : Completable = postDao.deletePostByDate(date)
}
