package com.jacksafblaze.roomdemo

data class UserMessage(val id: Int, val message: String)
data class SubscriberUiState(val messages: List<UserMessage> = emptyList())