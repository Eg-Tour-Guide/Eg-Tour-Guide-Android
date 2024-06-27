package com.egtourguide.home.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.presentation.navigation.AppScreen
import com.egtourguide.core.presentation.navigation.MainNavGraph

@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: AppScreen.Home.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = getBottomBarVisibility(currentDestination = currentDestination),
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                BottomBar(
                    selectedScreen = currentDestination,
                    onItemSelected = { route ->
                        navController.navigate(route = route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            MainNavGraph(
                navController = navController
            )
        }
    }
}

fun getBottomBarVisibility(currentDestination: String): Boolean {
    return currentDestination in listOf(
        AppScreen.Home.route,
        AppScreen.ToursList.route,
        AppScreen.LandmarksList.route,
        AppScreen.ArtifactsList.route,
        AppScreen.User.route,
        AppScreen.Search.route,
        AppScreen.SearchResults.route
    )
}