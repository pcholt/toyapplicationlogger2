package com.overswell.toy_application_logger_2

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(
            this, listOf(
                firebaseModule,
                viewModelModule
            )
        )
    }
}

val firebaseModule = module {
    single { FirebaseDatabase.getInstance() }
    single { get<FirebaseDatabase>().reference }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}