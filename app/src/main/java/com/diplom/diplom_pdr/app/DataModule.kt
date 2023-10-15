package com.diplom.diplom_pdr.app

import com.testtask.data.commands.AppCommandsImpl
import com.testtask.data.journals.RefuelingIntervalJournal
import com.testtask.data.journals.StatsJournal
import com.testtask.data.journals.TripJournal
import com.testtask.data.journals.impl.RefuelingIntervalJournalImpl
import com.testtask.data.journals.impl.StatsJournalImpl
import com.testtask.data.journals.impl.TripJournalImpl
import com.testtask.domain.commands.AppCommands
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single<AppCommands> {
        AppCommandsImpl(
            tripJournal = get(),
            refuelingIntervalJournal = get(),
            statsJournal = get(),
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }

    single<TripJournal> {
        TripJournalImpl(
            context = get(),
            db = (androidApplication().applicationContext as App).db.getDao())
    }

    single<RefuelingIntervalJournal> {
        RefuelingIntervalJournalImpl(
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }

    single<StatsJournal> {
        StatsJournalImpl(
            androidApplication(),
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }
}

