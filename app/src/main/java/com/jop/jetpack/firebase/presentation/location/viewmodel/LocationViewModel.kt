package com.jop.jetpack.firebase.presentation.location.viewmodel

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.jop.jetpack.firebase.presentation.location.view.LocationScreenEvent
import com.jop.jetpack.firebase.presentation.location.view.LocationScreenState
import com.jop.jetpack.firebase.utils.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.osmdroid.util.GeoPoint

class LocationViewModel(application: Application): ViewModel() {
    private val _state = MutableStateFlow(LocationScreenState())
    val state: StateFlow<LocationScreenState> = _state
    private val maxRadius = 100.0

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val locationManager = LocationManager(context = application, fusedLocationProviderClient = fusedLocationProviderClient)
    private val locationRequest =  LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
        .setWaitForAccurateLocation(false)
        .setMinUpdateDistanceMeters(10f)
        .build()

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            _state.value = _state.value.copy(personLocation = result.lastLocation)
            calculateDistance()
        }
    }

    fun onEvent(locationEvent: LocationScreenEvent){
        when(locationEvent){
            is LocationScreenEvent.GetLiveLocation -> {
                locationManager.getLiveLocation(locationRequest, locationCallback)
            }
            is LocationScreenEvent.StopLiveLocation -> {
                locationManager.stopLiveLocation(locationCallback)
            }
        }
    }

    private fun calculateDistance(){
        val personLocation = _state.value.personLocation
        val officeLocation = _state.value.officeLocation
        val results = FloatArray(1)

        Location.distanceBetween(
            personLocation!!.latitude,
            personLocation!!.longitude,
            officeLocation!!.latitude,
            officeLocation!!.longitude,
            results
        )

        _state.value = _state.value.copy(
            outOffRange = maxRadius < results[0]
        )
    }

    init {
        _state.value = _state.value.copy(
            officeLocation = GeoPoint(-7.32728, 112.79461)
        )
    }
}