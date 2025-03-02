package com.jop.jetpack.firebase.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.jop.jetpack.firebase.ui.route.Route

sealed class BottomNavItem(val route: String, val iconSelect: ImageVector, val iconUnselect: ImageVector, val label: String) {
    object Dashboard : BottomNavItem(Route.HOME_DASHBOARD, Icons.Default.Dashboard, Icons.Outlined.Dashboard, "Dashboard")
    object Location : BottomNavItem(Route.HOME_LOCATION, Icons.Default.LocationOn, Icons.Outlined.LocationOn, "Location")
}
