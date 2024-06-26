package com.egtourguide.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.presentation.navigation.AppGraph
import com.egtourguide.core.presentation.navigation.AppNavigation
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.DataStoreKeys.IS_LOGGED_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getFromDataStoreUseCase: GetFromDataStoreUseCase
    private var showSplash = true
    private var isLogged: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            showSplash
        }
        hideSplash()

        lifecycleScope.launch(Dispatchers.IO) {
            isLogged = getFromDataStoreUseCase(key = IS_LOGGED_KEY)
        }

        setContent {
            EGTourGuideTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    startDestination = getStartDestination()
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

    private fun getStartDestination(): String {
        return if (isLogged == true) AppGraph.Main.route
        else AppGraph.Auth.route
    }
}