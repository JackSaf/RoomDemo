package com.jacksafblaze.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase: RoomDatabase() {
    abstract fun subscriberDAO(): SubscriberDAO

    companion object{
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null
        fun getInstance(context: Context): SubscriberDatabase {
            return synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        SubscriberDatabase::class.java,
                        "SubscriberDatabase"
                    ).build()
                }
                instance
            }
        }
    }
}