package com.jacksafblaze.roomdemo

data class UserMessage(val id: Long, val message: String)
data class SubscriberUiState(val messages: List<UserMessage> = emptyList())