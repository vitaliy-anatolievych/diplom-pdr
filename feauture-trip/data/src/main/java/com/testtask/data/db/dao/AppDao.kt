package com.testtask.data.db.dao

import androidx.room.*
import com.testtask.data.db.models.FuelDBModel
import com.testtask.data.db.models.RefuelingIntervalDBEntity
import com.testtask.data.db.models.SpeedDBModel
import com.testtask.data.db.models.StatsJournalDBEntity
import com.testtask.data.db.models.TripDataDBEntity

@Dao
interface AppDao {

    /** Журнал поездки */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTripJournal(trip: TripDataDBEntity)

    @Query("DELETE FROM trip_journal")
    fun clearTripJournal()

    @Query("SELECT * FROM trip_journal")
    fun getTripJournal(): List<TripDataDBEntity>

    /** Журнал межзаправочного интервала */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRefuelingJournal(journal: RefuelingIntervalDBEntity)

    @Query("DELETE FROM RefuelingJournal")
    fun clearRefuelingJournal()

    @Query("SELECT * FROM RefuelingJournal")
    fun getRefuelingJournal(): List<RefuelingIntervalDBEntity>

    /** Журнал Статистика */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStatsJournal(journal: StatsJournalDBEntity)

    @Query("SELECT * FROM StatsJournal")
    fun getStatsJournal(): List<StatsJournalDBEntity>

    @Query("DELETE FROM StatsJournal")
    fun clearStatsJournal()

    /** Данные о скорости */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setRecommendedSpeed(speed: SpeedDBModel)

    @Query("SELECT speed FROM SpeedValue WHERE id = 0")
    fun getRecommendedSpeed(): Int?

    @Query("DELETE FROM SpeedValue")
    fun clearRecommendedSpeed()

    /** Данные о топливе */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun fillFuelTank(fuel: FuelDBModel)

    @Query("SELECT * FROM FuelVolume")
    fun getFuelVolume(): FuelDBModel?

    @Query("DELETE FROM FuelVolume")
    fun clearFuelVolume()
}