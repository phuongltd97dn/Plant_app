package com.example.week_7.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cultivation")
data class Cultivation(
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0,
    var userGrowId: Int? = 0,
    var plantId: String? = "",
    var dateCultivation: String = "",
    var dateWatering: String? = ""
) : Serializable