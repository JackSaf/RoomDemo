package com.jacksafblaze.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ActivityMainViewModelFactory(private val repository: Repository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ActivityMainViewModel::class.java)){
            return ActivityMainViewModel(repository) as T
        }
        throw IllegalArgumentException("View model is not found")
    }
}