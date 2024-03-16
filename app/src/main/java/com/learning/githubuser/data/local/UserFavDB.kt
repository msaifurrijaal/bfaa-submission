package com.learning.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learning.githubuser.data.model.ResponseDetailUser

@Database(entities = [ResponseDetailUser::class], version = 1, exportSchema = false)
abstract class UserFavDB : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        var INSTANCE: UserFavDB? = null

        @Synchronized
        fun getInstance(context: Context): UserFavDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    UserFavDB::class.java,
                    "game_db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as UserFavDB
        }
    }
}