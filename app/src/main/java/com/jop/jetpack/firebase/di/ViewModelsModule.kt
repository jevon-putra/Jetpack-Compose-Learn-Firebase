package com.jop.jetpack.firebase.di

import com.jop.jetpack.firebase.MainViewModel
import com.jop.jetpack.firebase.presentation.dashboard.viewmodel.DashboardViewModel
import com.jop.jetpack.firebase.presentation.location.viewmodel.LocationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel<LocationViewModel> { LocationViewModel(get()) }
    viewModel<DashboardViewModel> { DashboardViewModel() }
    viewModel<MainViewModel> { MainViewModel() }
}