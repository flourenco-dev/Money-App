package com.example.minimoney.ui.injection

import com.example.minimoney.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        MainViewModel(repository = get())
    }
}