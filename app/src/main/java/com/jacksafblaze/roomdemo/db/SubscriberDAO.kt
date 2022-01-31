package com.jacksafblaze.roomdemo.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long
    @Insert
    suspend fun insertSubscribers(vararg subscribers: Subscriber)
    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int
    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int
    @Query("DELETE FROM subscriber_data_table")
    suspend fun clearAll(): Int
    @Query("SELECT * FROM subscriber_data_table")
    fun getAll(): Flow<List<Subscriber>>
}