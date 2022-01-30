package com.jacksafblaze.roomdemo.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber)
    @Insert
    suspend fun insertSubscribers(vararg subscribers: Subscriber)
    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)
    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)
    @Query("DELETE FROM subscriber_data_table")
    suspend fun clearAll()
    @Query("SELECT * FROM subscriber_data_table")
    fun getAll(): Flow<List<Subscriber>>
}