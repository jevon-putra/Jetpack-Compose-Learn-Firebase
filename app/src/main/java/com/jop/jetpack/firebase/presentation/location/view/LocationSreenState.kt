package com.jop.jetpack.firebase.presentation.location.view

import android.location.Location
import org.osmdroid.util.GeoPoint

data class LocationScreenState(
    val personLocation: Location? = null,
    val officeLocation: GeoPoint? = null,
    val outOffRange: Boolean = false,
)