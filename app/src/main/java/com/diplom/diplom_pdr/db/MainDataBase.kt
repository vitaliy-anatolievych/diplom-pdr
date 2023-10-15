package com.diplom.diplom_pdr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItem

@TypeConverters(RoomTypeConverters::class)
@Database(entities = [TaskItem::class, ThemeItem::class, TestsResultEntity::class, DriveStatsModel::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "pdr_tests.db"
                ).build()
                instance
            }
        }
    }
}