package com.example.week_7.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.week_7.models.Cultivation
import com.example.week_7.models.Plant
import com.example.week_7.models.User

@Database(entities = [User::class, Plant::class, Cultivation::class], version = 1)
@TypeConverters(MyTypeConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract val myDAO: MyDAO

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "week7.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}