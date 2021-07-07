package com.example.week_7.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.week_7.models.Cultivation
import com.example.week_7.models.Plant
import com.example.week_7.models.User

@Dao
interface MyDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(vararg user: User?)

    @Query("SELECT * FROM user")
    fun getUser(): User?

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUserWithId(id: Int?): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlants(plants: List<Plant>?)

    @Query("SELECT * FROM plant")
    fun getAllPlants(): List<Plant>?

    @Query("SELECT * FROM plant WHERE plantId = :id")
    fun getPlantWithId(id: String?): Plant?

    @Query("SELECT * FROM plant WHERE name = :name")
    fun getPlantWithName(name: String?): Plant?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCultivation(vararg cultivation: Cultivation?)

    @Query("SELECT * FROM cultivation INNER JOIN plant ON cultivation.plantId = plant.plantId ORDER BY cultivation.Id DESC")
    fun getAllCultivations(): List<Cultivation>?
}