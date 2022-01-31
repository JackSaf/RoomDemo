package com.jacksafblaze.roomdemo

import com.jacksafblaze.roomdemo.db.Subscriber
import com.jacksafblaze.roomdemo.db.SubscriberDAO
import kotlinx.coroutines.flow.stateIn

class Repository(private val dao: SubscriberDAO) {
    val subscribers = dao.getAll()

    suspend fun insert(subscriber: Subscriber): Long{
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int{
        return dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int{
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun clearAll(): Int{
        return dao.clearAll()
    }
}