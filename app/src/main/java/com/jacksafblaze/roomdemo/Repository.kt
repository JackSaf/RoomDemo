package com.jacksafblaze.roomdemo

import com.jacksafblaze.roomdemo.db.Subscriber
import com.jacksafblaze.roomdemo.db.SubscriberDAO
import kotlinx.coroutines.flow.stateIn

class Repository(private val dao: SubscriberDAO) {
    val subscribers = dao.getAll()

    suspend fun insert(subscriber: Subscriber){
        dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber){
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun clearAll(){
        dao.clearAll()
    }
}