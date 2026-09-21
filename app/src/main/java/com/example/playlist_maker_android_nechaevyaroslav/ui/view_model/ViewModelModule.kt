package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
}