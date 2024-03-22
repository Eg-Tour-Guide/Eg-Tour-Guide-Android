package com.egtourguide.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordScreen
import com.egtourguide.auth.presentation.login.LoginScreen
import com.egtourguide.auth.presentation.otp.OtpScreen
import com.egtourguide.auth.presentation.resetPassword.ResetPasswordScreen
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
            //TODO Navigate to reset password
            OtpScreen()
        }

        composable(route = AppScreen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(route = AppScreen.SignUp.route)
                },
                onNavigateToForgetPassword = {
                    navController.navigate(route = AppScreen.ForgetPassword.route)
                },
                onNavigateToHome = {
                    navController.navigate(route = AppScreen.Welcome.route) //TODO(Edit to home)
                })
        }

        composable(route = AppScreen.ForgetPassword.route) {
            ForgotPasswordScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToOTP = {
                    navController.navigate(route = AppScreen.OTP.route){
                        popUpTo(route = AppScreen.Login.route)
                    }
                }
            )
        }

        composable(route = AppScreen.ResetPassword.route) {
            ResetPasswordScreen()
        }
    }
}