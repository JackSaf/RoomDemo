package com.jacksafblaze.roomdemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.roomdemo.db.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ActivityMainViewModel(private val repository: Repository): ViewModel() {
    val subscribers = repository.subscribers

    private var isUpdateOrDelete = MutableStateFlow(false)

    private lateinit var subscriberForUpdateOrDelete: Subscriber

    var subscriberName = MutableStateFlow<String?>(null)

    val subscriberEmail = MutableStateFlow<String?>(null)

    val saveOrUpdateButtonText = MutableStateFlow("Save")

    val clearAllOrDeleteButtonText = MutableStateFlow("Clear all")

    private val statusMessage = MutableStateFlow<Event<String?>>(Event(null))

    val message = statusMessage.asStateFlow()

    init{
        initUpdateAndDeleteState()
    }
    fun saveOrUpdate(){
        if(isUpdateOrDelete.value){
            update(subscriberForUpdateOrDelete)
            isUpdateOrDelete.value = false
        }
        else {
            val name = subscriberName.value
            val email = subscriberEmail.value
            if (!name.isNullOrBlank() && !email.isNullOrBlank()) {
                insert(Subscriber(0, name, email))
            }
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete.value){
            delete(subscriberForUpdateOrDelete)
            isUpdateOrDelete.value = false
        }
        else {
            clearAll()
        }
    }
    private fun initUpdateAndDeleteState() = viewModelScope.launch {
        isUpdateOrDelete.collectLatest{
            Log.i("Flow", "value collected")
            if(it){
                saveOrUpdateButtonText.value = "update"
                clearAllOrDeleteButtonText.value = "delete"
            }
            else{
                saveOrUpdateButtonText.value = "save"
                clearAllOrDeleteButtonText.value = "clear all"
                subscriberName.value = null
                subscriberEmail.value = null
            }
        }
    }
    fun initUpdateAndDelete(subscriber: Subscriber){
        subscriberName.value = subscriber.name
        subscriberEmail.value = subscriber.email
        isUpdateOrDelete.value = true
        subscriberForUpdateOrDelete = subscriber
        Log.i("Flow", "value assigned")
    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
        statusMessage.value = Event("Subscriber inserted successfully")
    }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        subscriber.name = subscriberName.value!!
        subscriber.email = subscriberEmail.value!!
        repository.update(subscriber)
        statusMessage.value = Event("Subscriber updated successfully")

    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        statusMessage.value = Event("Subscriber deleted successfully")
    }

    private fun clearAll() = viewModelScope.launch {
        repository.clearAll()
        statusMessage.value = Event("All subscribers deleted successfully")
    }
}