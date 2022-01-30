package com.jacksafblaze.roomdemo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var email: String
    )