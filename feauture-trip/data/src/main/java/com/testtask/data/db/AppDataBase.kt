package com.testtask.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.FuelDBModel
import com.testtask.data.db.models.RefuelingIntervalDBEntity
import com.testtask.data.db.models.SpeedDBModel
import com.testtask.data.db.models.StatsJournalDBEntity
import com.testtask.data.db.models.TripDataDBEntity

@Database(
    entities = [TripDataDBEntity::class, RefuelingIntervalDBEntity::class,
        FuelDBModel::class, SpeedDBModel::class, StatsJournalDBEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        private val LOCK = Any() // Для синхронизации доступа к базе данных из разных потоков
        private const val DB_NAME = "diplom_proj.db"

        fun getInstance(context: Context): AppDataBase {

            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}