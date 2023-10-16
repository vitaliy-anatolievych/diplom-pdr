package com.diplom.diplom_pdr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItem
import com.diplom.diplom_pdr.models.UserEntity

@TypeConverters(RoomTypeConverters::class)
@Database(
    entities = [TaskItem::class, ThemeItem::class, TestsResultEntity::class, DriveStatsModel::class, Answer::class, UserEntity::class],
    version = 2,
    exportSchema = true
)
abstract class MainDataBase : RoomDatabase() {

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