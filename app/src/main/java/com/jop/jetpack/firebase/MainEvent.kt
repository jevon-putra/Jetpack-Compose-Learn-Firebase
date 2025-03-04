package com.jop.jetpack.firebase

import androidx.navigation.NavHostController

sealed interface MainEvent {
    data class OnSaveDataNotification(val targetValue: String, val targetData: String): MainEvent
    data class OnClickNotification(val navHostController: NavHostController): MainEvent
}