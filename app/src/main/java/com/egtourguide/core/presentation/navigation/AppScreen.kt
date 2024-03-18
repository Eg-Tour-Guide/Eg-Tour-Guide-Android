package com.egtourguide.core.presentation.navigation

sealed class AppScreen(val route: String) {

    // Auth Screens
    object Login : AppScreen(route = ScreenRoutes.LOGIN_SCREEN_ROUTE)
    object SignUp : AppScreen(route = ScreenRoutes.SIGNUP_SCREEN_ROUTE)
    object Welcome : AppScreen(route = ScreenRoutes.WELCOME_SCREEN_ROUTE)
    object OTP : AppScreen(route = ScreenRoutes.OTP_SCREEN_ROUTE)
    object ForgetPassword : AppScreen(route = ScreenRoutes.FORGET_PASSWORD_SCREEN_ROUTE)
    object CreatePassword : AppScreen(route = ScreenRoutes.CREATE_PASSWORD_SCREEN_ROUTE)
}
