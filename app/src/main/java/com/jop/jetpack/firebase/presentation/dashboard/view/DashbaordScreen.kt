package com.jop.jetpack.firebase.presentation.dashboard.view

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.jop.jetpack.firebase.MainEvent
import com.jop.jetpack.firebase.MainState
import com.jop.jetpack.firebase.ui.component.PermissionDialog
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DashboardScreen(navController: NavHostController, mainEvent: (MainEvent) -> Unit, state: StateFlow<MainState>){
    val context = LocalContext.current
    val showAlertPermission = remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (!isGranted) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.POST_NOTIFICATIONS)){
                        Toast.makeText(context,"Izinkan aplikasi untuk mengirim notifikasi", Toast.LENGTH_SHORT).show()
                    } else {
                        showAlertPermission.value = true
                    }
                }
            }
        )

        LaunchedEffect(true) {
            permissionLauncher.launch(notificationPermission)
        }
    }

    if(showAlertPermission.value){
        PermissionDialog(
            title = "Peringatan",
            message = "Izinkan aplikasi untuk mengirim notifikasi",
            onDismiss = { showAlertPermission.value = false }
        )
    }

    LaunchedEffect(true){
        state.value.let {
            if(it.targetData.isNotEmpty() && it.targetValue.isNotEmpty()){
                mainEvent(MainEvent.OnClickNotification(navController))
            }
        }
    }

}
