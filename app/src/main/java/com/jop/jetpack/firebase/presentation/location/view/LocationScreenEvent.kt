package com.jop.jetpack.firebase.presentation.location.view

sealed interface LocationScreenEvent {
    data object GetLiveLocation: LocationScreenEvent
    data object StopLiveLocation: LocationScreenEvent
}