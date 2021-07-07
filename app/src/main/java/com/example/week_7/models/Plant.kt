package com.example.week_7.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant")
data class Plant(
    @PrimaryKey
    var plantId: String = "",
    var name: String = "",
    var description: String = "",
    var growZoneNumber: Int = 0,
    var wateringInterval: Int = 0,
    var imageUrl: String? = null
)