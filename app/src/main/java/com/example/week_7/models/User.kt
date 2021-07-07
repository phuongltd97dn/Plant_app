package com.example.week_7.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,
    var userName: String = "",
    var university: String = "",
    var homeTown: String = "",
    var avatar: Bitmap? = null
)