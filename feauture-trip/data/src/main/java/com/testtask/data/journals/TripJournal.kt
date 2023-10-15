package com.testtask.data.journals

interface TripJournal {

    suspend fun startTrip()
    suspend fun stopTrip()
    suspend fun getSpeed(listener: ((Int) -> Unit))
    suspend fun getDistance(listener: ((Double) -> Unit))
    suspend fun isHaveNotes(): Boolean
}