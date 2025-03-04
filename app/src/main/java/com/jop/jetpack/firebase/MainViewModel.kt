package com.jop.jetpack.firebase

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.OnSaveDataNotification -> {
                _state.value = _state.value.copy(
                    targetData = event.targetData,
                    targetValue = event.targetValue
                )
            }
            is MainEvent.OnClickNotification -> {
                event.navHostController.navigate(_state.value.targetValue)
                _state.value = _state.value.copy(
                    targetData = "",
                    targetValue = ""
                )
            }
        }
    }
}