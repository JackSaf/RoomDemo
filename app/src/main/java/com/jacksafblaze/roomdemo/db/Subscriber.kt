package com.jacksafblaze.roomdemo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String
    )