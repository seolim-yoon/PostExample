package com.example.postexample.data.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.postexample.data.database.dao.UserInfoDao
import com.example.postexample.data.database.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}