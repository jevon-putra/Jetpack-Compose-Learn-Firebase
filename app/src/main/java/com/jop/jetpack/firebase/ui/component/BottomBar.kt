package com.jop.jetpack.firebase.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jop.jetpack.firebase.data.BottomNavItem

@Composable
fun BottomBar(modifier: Modifier, navController: NavHostController){
    val screens = listOf(BottomNavItem.Dashboard, BottomNavItem.Location)

    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(
                        text = screen.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if(currentRoute == screen.route) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = if(currentRoute == screen.route) screen.iconSelect else screen.iconUnselect,
                        contentDescription = ""
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}