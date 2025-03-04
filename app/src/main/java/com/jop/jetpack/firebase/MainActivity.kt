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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.jop.jetpack.firebase.ui.component.BottomBar
import com.jop.jetpack.firebase.ui.route.NavigationRoute
import com.jop.jetpack.firebase.ui.theme.ComposeFirebaseTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    companion object {
        @JvmStatic
        fun newInstance(context: Context?, targetValue: String = "",  targetData: String = ""): Intent {
            val bundle = Bundle().apply {
                putString("targetValue", targetValue)
                putString("targetData", targetData)
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

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Black.toArgb())
        )

        setContent {
            val navController = rememberNavController()
            val viewModel = koinViewModel<MainViewModel>()

            // COLD MODE
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> {
                    it.extras?.let { data ->
                        val targetValue = data.getString("targetValue", "")
                        val targetData = data.getString("targetData", "")

                        if(targetValue.isNotEmpty() && targetData.isNotEmpty())  {
                            navController.navigate(targetValue)
                        }
                    }
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }

            // HOT MODE
            LaunchedEffect(true) {
                intent.extras?.let {
                    val targetValue = it.getString("targetValue", "")
                    val targetData = it.getString("targetData", "")
                    viewModel.onEvent(MainEvent.OnSaveDataNotification(targetValue, targetData))
                }
            }

            ComposeFirebaseTheme {
                KoinAndroidContext {
                    val showBottomBar = remember { mutableStateOf(false) }
                    Column {
                        NavigationRoute(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            navController = navController,
                            mainViewModel = viewModel,
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