package com.egtourguide.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.presentation.navigation.AppNavigation
import com.egtourguide.core.presentation.navigation.AppScreen
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var showSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            showSplash
        }
        hideSplash()

        setContent {
            EGTourGuideTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    startDestination = AppScreen.Welcome.route
                )
            }
        }
    }

    private fun hideSplash() {
        lifecycleScope.launch {
            delay(1000L)
            showSplash = false
        }
    }
}