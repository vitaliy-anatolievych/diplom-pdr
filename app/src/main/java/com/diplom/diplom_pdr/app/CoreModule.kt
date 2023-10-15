package com.diplom.diplom_pdr.app

import com.diplom.diplom_pdr.controller.LocalStorage
import com.diplom.diplom_pdr.db.Dao
import com.diplom.diplom_pdr.db.MainDataBase
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.QuestViewModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.TestQuestionsViewModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.TestStatsViewModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.TripViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val core = module {

    viewModel {
        MainViewModel(
            localStorage = get(),
            appDb = (androidApplication() as App).db.getDao()
        )
    }

    viewModel {
        QuestViewModel(
            localStorage = get()
        )
    }

    viewModel {
        TestStatsViewModel(
            localStorage = get()
        )
    }

    viewModel {
        TripViewModel(
            startTripUseCase = get(),
            stopTripUseCase = get(),
            fillFuelTankUseCase = get(),
            listenSpeedUseCase = get(),
            setSpeedUseCase = get(),
            getRecommendSpeedUseCase = get(),
            getStatsJournalUseCase = get(),
            deleteStatsJournalUseCase = get(),
            isHaveNotesTripJournalUseCase = get(),
            listenDistanceUseCase = get(),
        )
    }

    viewModel {
        TestQuestionsViewModel(
            localStorage = get()
        )
    }

    single {
        LocalStorage(
            context = androidContext(),
            dao = get()
        )
    }

    single<Dao> {
        MainDataBase.getDataBase(context = androidContext()).getDao()
    }

}