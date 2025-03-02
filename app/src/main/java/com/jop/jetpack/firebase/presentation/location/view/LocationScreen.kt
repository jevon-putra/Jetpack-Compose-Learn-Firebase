package com.jop.jetpack.firebase.presentation.location.view

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.jop.jetpack.firebase.R
import com.jop.jetpack.firebase.ui.component.CustomPrimaryButton
import com.jop.jetpack.firebase.ui.component.CustomToolbar
import com.jop.jetpack.firebase.ui.component.PermissionDialog
import com.jop.jetpack.firebase.utils.CircleOverlay
import kotlinx.coroutines.delay
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(navController: NavHostController, state: LocationScreenState, onEvent: (LocationScreenEvent) -> Unit){
    val context = LocalContext.current
    val showAlertPermission = remember { mutableStateOf(false) }
    val locationPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.all { it.value }) {
                onEvent(LocationScreenEvent.GetLiveLocation)
            } else {
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(context,"Izinkan aplikasi untuk mengakses kamera", Toast.LENGTH_SHORT).show()
                } else {
                    showAlertPermission.value = true
                }
            }
        }
    )

    if(showAlertPermission.value){
        PermissionDialog(
            title = "Peringatan",
            message = "Izinkan aplikasi untuk lokasi terkini anda",
            onDismiss = { showAlertPermission.value = false }
        )
    }

    DisposableEffect(true) {
        onDispose {
            onEvent(LocationScreenEvent.StopLiveLocation)
        }
    }

    LaunchedEffect(true) {
        permissionLauncher.launch(locationPermissions)
    }

    Scaffold(
        topBar = {
            CustomToolbar(
                title = "Absensi",
                color = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.safeContent)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ){
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ){
                AndroidView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    factory = { context ->
                        val mapView = MapView(context)
                        val mapController = mapView.controller as MapController
                        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

                        val circle = CircleOverlay(state.officeLocation?.latitude ?: 0.0, state.officeLocation?.longitude ?: 0.0, 100f)

                        mapController.setZoom(19)
                        mapController.animateTo(state.officeLocation)
                        mapView.overlays.add(circle)
                        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                        mapView
                    },
                    update = { mapView ->
                        mapView.zoomController.setZoomInEnabled(false)
                        mapView.zoomController.setZoomOutEnabled(false)
                        mapView.setMultiTouchControls(false)

                        if(state.personLocation != null){
                            val centerPerson = GeoPoint(state.personLocation.latitude, state.personLocation.longitude)
                            val mapController = mapView.controller as MapController

                            val locationPerson = Marker(mapView).apply {
                                setPosition(centerPerson)
                                icon = ContextCompat.getDrawable(context, R.drawable.ic_person)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }

                            mapController.setZoom(19)
                            mapController.animateTo(centerPerson)

                            if(mapView.overlays.size > 1) mapView.overlays.removeAt(1)
                            mapView.overlays.add(locationPerson)
                        }
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = state.outOffRange
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(4.dp),
                        text = "Kamu berada diluar area kantor",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DateTimeDisplay()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        4.dp,
                                        alignment = Alignment.CenterHorizontally
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Text(
                                        text = "Clock In",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "08:30",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        4.dp,
                                        alignment = Alignment.CenterHorizontally
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Default.CheckCircleOutline,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Text(
                                        text = "Clock Out",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "16:30",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }

                    CustomPrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "CLOCK IN",
                        onClick = {
                            onEvent(LocationScreenEvent.StopLiveLocation)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DateTimeDisplay(){
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    var date by rememberSaveable{ mutableStateOf(Date()) }

    LaunchedEffect(true) {
        while (true){
            date = Date()
            delay(1000)
        }
    }

    Column{
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = timeFormat.format(date),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = dateFormat.format(date),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        )
    }
}