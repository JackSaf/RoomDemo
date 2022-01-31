package com.jacksafblaze.roomdemo

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.roomdemo.db.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class ActivityMainViewModel(private val repository: Repository) : ViewModel() {
    val subscribers = repository.subscribers

    private var isUpdateOrDelete = MutableStateFlow(false)

    private lateinit var subscriberForUpdateOrDelete: Subscriber

    var subscriberName = MutableStateFlow<String?>(null)

    val subscriberEmail = MutableStateFlow<String?>(null)

    val saveOrUpdateButtonText = MutableStateFlow("Save")

    val clearAllOrDeleteButtonText = MutableStateFlow("Clear all")

    val subscriberUiState = MutableStateFlow(SubscriberUiState())

    init {
        initUpdateAndDeleteState()
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete.value) {
            update(subscriberForUpdateOrDelete)
            isUpdateOrDelete.value = false
        } else {
            if(validateInput()){
                insert(Subscriber(0, subscriberName.value.toString(), subscriberEmail.value.toString()))
                subscriberName.value = null
                subscriberEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete.value) {
            delete(subscriberForUpdateOrDelete)
            isUpdateOrDelete.value = false
        } else {
            clearAll()
        }
    }

    private fun initUpdateAndDeleteState() = viewModelScope.launch {
        isUpdateOrDelete.collectLatest {
            Log.i("Flow", "value collected")
            if (it) {
                saveOrUpdateButtonText.value = "update"
                clearAllOrDeleteButtonText.value = "delete"
            } else {
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
                UUID.randomUUID().mostSignificantBits, message
            )
            currentUiState.copy(messages = messages)
        }
    }

    fun userMessageShown(messageId: Long) {
        subscriberUiState.update { currentUiState ->
            val newMessages = currentUiState.messages.filterNot { it.id == messageId }
            currentUiState.copy(messages = newMessages)
        }
    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val idOfInserted = repository.insert(subscriber)
        postMessage("Subscriber $idOfInserted inserted successfully")
    }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        if(validateInput()){
            subscriber.name = subscriberName.value!!
            subscriber.email = subscriberEmail.value!!
            val numOfUpdated = repository.update(subscriber)
            postMessage("$numOfUpdated subscriber updated successfully")
        }
    }

    private fun validateInput(): Boolean{
        val name = subscriberName.value
        val email = subscriberEmail.value
        return if (name.isNullOrBlank()) {
            postMessage("Enter subscriber's name")
            false
        } else if (email.isNullOrBlank()) {
            postMessage("Enter subscriber's email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            postMessage("Email address is incorrect")
            false
        } else {
            true
        }
    }
    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val numOfDeleted = repository.delete(subscriber)
        postMessage("$numOfDeleted subscriber deleted successfully")
    }

    private fun clearAll() = viewModelScope.launch {
        val numOfDeleted = repository.clearAll()
        postMessage("$numOfDeleted subscribers deleted successfully")
    }

}