package com.egtourguide.core.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.presentation.navigation.AppNavigation
import com.egtourguide.core.presentation.navigation.AppScreen
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Fix this!!
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.setDecorFitsSystemWindows(false)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
//            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom +
//                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
//            view.updatePadding(bottom = bottom, top = top)
//            insets
//        }

        setContent {
            EGTourGuideTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    startDestination = AppScreen.SignUp.route
                )
            }
        }
    }
}