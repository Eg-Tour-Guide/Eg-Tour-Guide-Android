package com.egtourguide.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.presentation.navigation.AppNavigation
import com.egtourguide.core.presentation.navigation.AppScreen
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}