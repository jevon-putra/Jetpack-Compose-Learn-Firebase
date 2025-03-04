package com.jop.jetpack.firebase.ui.route

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jop.jetpack.firebase.MainViewModel
import com.jop.jetpack.firebase.presentation.dashboard.view.DashboardScreen
import com.jop.jetpack.firebase.presentation.location.view.LocationScreen
import com.jop.jetpack.firebase.presentation.location.viewmodel.LocationViewModel
import com.jop.jetpack.firebase.presentation.welcome.view.WelcomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoute(
    modifier: Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    showBottomBar: (Boolean) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.SPLASH,
        enterTransition = { scaleIn(tween(700), initialScale = 0.5f) + fadeIn(tween(50)) },
        exitTransition = { scaleOut(tween(500), targetScale = 0.5f) + fadeOut(tween(50)) },
        popEnterTransition = { scaleIn(tween(700), initialScale = 0.5f) + fadeIn(tween(50)) },
        popExitTransition = { scaleOut(tween(500), targetScale = 0.5f) + fadeOut(tween(50)) }
    ){
        navigation(
            startDestination = Route.HOME_DASHBOARD,
            route = Route.HOME
        ){
            composable(route = Route.HOME_DASHBOARD){
                showBottomBar(true)
                DashboardScreen(navController = navController, mainViewModel::onEvent, mainViewModel.state)
            }

            composable(route = Route.HOME_LOCATION){
                showBottomBar(true)
                val viewModel: LocationViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                LocationScreen(navController = navController, state, viewModel::onEvent)
            }
        }

        composable(route = Route.SPLASH){
            showBottomBar(false)
            WelcomeScreen(navController = navController)
        }
    }
}