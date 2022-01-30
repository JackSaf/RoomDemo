package com.jacksafblaze.roomdemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.roomdemo.db.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class ActivityMainViewModel(private val repository: Repository): ViewModel() {
    val subscribers = repository.subscribers

    private var isUpdateOrDelete = MutableStateFlow(false)

    private lateinit var subscriberForUpdateOrDelete: Subscriber

    var subscriberName = MutableStateFlow<String?>(null)

    val subscriberEmail = MutableStateFlow<String?>(null)

    val saveOrUpdateButtonText = MutableStateFlow("Save")

    val clearAllOrDeleteButtonText = MutableStateFlow("Clear all")

    val subscriberUiState = MutableStateFlow(SubscriberUiState())

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
                subscriberName.value = null
                subscriberEmail.value = null
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
    fun initUpdateAndDelete(subscriber: Subscriber) = viewModelScope.launch {
        subscriberName.value = subscriber.name
        subscriberEmail.value = subscriber.email
        isUpdateOrDelete.value = true
        subscriberForUpdateOrDelete = subscriber
        Log.i("Flow", "value assigned")
    }

    private fun postMessage(message: String) = viewModelScope.launch {
        subscriberUiState.update { currentUiState ->
            val messages = currentUiState.messages + UserMessage(
            UUID.randomUUID().mostSignificantBits, message)
            currentUiState.copy(messages = messages)
        }
    }

    fun userMessageShown(messageId : Long){
        subscriberUiState.update{ currentUiState ->
            val newMessages = currentUiState.messages.filterNot{ it.id == messageId }
            currentUiState.copy(messages = newMessages)
        }
    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
        postMessage("Subscriber inserted successfully")
    }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        subscriber.name = subscriberName.value!!
        subscriber.email = subscriberEmail.value!!
        repository.update(subscriber)
        postMessage("Subscriber updated successfully")

    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        postMessage("Subscriber deleted successfully")
    }

    private fun clearAll() = viewModelScope.launch {
        repository.clearAll()
        postMessage("All subscribers deleted successfully")
    }
}