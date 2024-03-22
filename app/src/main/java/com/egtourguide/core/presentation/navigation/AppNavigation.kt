package com.egtourguide.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.egtourguide.auth.presentation.login.LoginScreen
import com.egtourguide.auth.presentation.otp.OtpScreen
import com.egtourguide.auth.presentation.signup.SignUpScreen
import com.egtourguide.auth.presentation.welcome.WelcomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AppScreen.Login.route)
                })
        }

        composable(route = AppScreen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(route = AppScreen.Login.route)
                }, onNavigateToOTP = {
                    navController.navigate(route = AppScreen.OTP.route)
                }
            )
        }

        composable(route = AppScreen.OTP.route) {
            OtpScreen()
        }

        composable(route = AppScreen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(route = AppScreen.SignUp.route)
                },
                onNavigateToForgetPassword = {
                    navController.navigate(route = AppScreen.SignUp.route) //TODO(Edit to forget password)
                },
                onNavigateToHome = {
                    navController.navigate(route = AppScreen.Welcome.route) //TODO(Edit to home)
                })
        }
    }
}