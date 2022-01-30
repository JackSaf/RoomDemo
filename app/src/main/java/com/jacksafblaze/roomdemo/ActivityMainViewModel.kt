package com.jacksafblaze.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.roomdemo.db.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityMainViewModel(private val repository: Repository): ViewModel() {
    val subscribers = repository.subscribers

    var subscriberName = MutableStateFlow<String?>(null)

    val subscriberEmail = MutableStateFlow<String?>(null)

    val saveOrUpdateButtonText = MutableStateFlow("Save")

    val clearAllOrDeleteButtonText = MutableStateFlow("Delete")


    fun saveOrUpdate(){
        val name = subscriberName.value
        val email = subscriberEmail.value
        if(!name.isNullOrBlank() && !email.isNullOrBlank()){
            insert(Subscriber(0, name, email))
            subscriberName.value = null
            subscriberEmail.value = null
        }
    }

    fun clearAllOrDelete(){
        clearAll()
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
    }

    fun clearAll() = viewModelScope.launch {
        repository.clearAll()
    }
}