package com.jop.jetpack.firebase.presentation.welcome.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.jop.jetpack.firebase.ui.route.Route
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavHostController){

    LaunchedEffect(true){
        delay(1500)
        navController.navigate(Route.HOME){
            popUpTo(navController.graph.id){ inclusive = true }
        }
    }

    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Selamat Datang Cuy",
        style = MaterialTheme.typography.titleLarge.copy(
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    )
}