package com.diplom.diplom_pdr.app

import com.testtask.domain.usecases.DeleteStatsJournal
import com.testtask.domain.usecases.FillFuelTank
import com.testtask.domain.usecases.GetRecommendSpeed
import com.testtask.domain.usecases.GetStatsJournal
import com.testtask.domain.usecases.IsHaveNotesTripJournal
import com.testtask.domain.usecases.ListenDistance
import com.testtask.domain.usecases.ListenSpeed
import com.testtask.domain.usecases.SetSpeed
import com.testtask.domain.usecases.StartTrip
import com.testtask.domain.usecases.StopTrip
import org.koin.dsl.module

val domainModule = module {

    single {
        StartTrip(appCommands = get())
    }

    single {
        StopTrip(appCommands = get())
    }

    single {
        FillFuelTank(appCommands = get())
    }

    single {
        ListenSpeed(appCommands = get())
    }

    single {
        ListenDistance(appCommands = get())
    }

    single {
        SetSpeed(appCommands = get())
    }

    single {
        GetRecommendSpeed(appCommands = get())
    }

    single {
        GetStatsJournal(appCommands = get())
    }

    single {
        DeleteStatsJournal(appCommands = get())
    }

    single {
        IsHaveNotesTripJournal(appCommands = get())
    }
}