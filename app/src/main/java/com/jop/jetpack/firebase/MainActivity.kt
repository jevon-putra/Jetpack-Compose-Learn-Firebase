package com.jop.jetpack.firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.jop.jetpack.firebase.ui.component.BottomBar
import com.jop.jetpack.firebase.ui.route.NavigationRoute
import com.jop.jetpack.firebase.ui.theme.ComposeFirebaseTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    companion object {
        @JvmStatic
        fun newInstance(context: Context?, targetValue: String): Intent {
            val bundle = Bundle().apply {
                putString("targetValue", targetValue)
            }
            return Intent(context, MainActivity::class.java).putExtras(bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            Log.e("TEST", "TOKEN ${it.result}")
        }.addOnFailureListener {
            Log.e("TEST", "Failed to get token ${it.message}")
        }

        intent.extras?.let {

        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Black.toArgb())
        )

        setContent {
            ComposeFirebaseTheme {
                KoinAndroidContext {
                    val navController = rememberNavController()
                    val showBottomBar = remember { mutableStateOf(false) }

                    Column {
                        NavigationRoute(
                            navController = navController,
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            showBottomBar = {
                                showBottomBar.value = it
                            }
                        )

                        AnimatedVisibility(visible = showBottomBar.value) {
                            BottomBar(modifier = Modifier, navController = navController)
                        }
                    }
                }
            }
        }
    }
}