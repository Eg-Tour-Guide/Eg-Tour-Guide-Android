package com.egtourguide.core.presentation.navigation

object ScreenRoutes {

    // Auth Routes
    const val WELCOME_SCREEN_ROUTE = "WELCOME_SCREEN_ROUTE"
    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"
    const val SIGNUP_SCREEN_ROUTE = "SIGNUP_SCREEN_ROUTE"
    const val OTP_SCREEN_ROUTE = "OTP_SCREEN_ROUTE/{code}/{fromSignup}/{name}/{email}/{phone}/{password}"
    const val FORGET_PASSWORD_SCREEN_ROUTE = "FORGET_PASSWORD_SCREEN_ROUTE"
    const val RESET_PASSWORD_SCREEN_ROUTE = "RESET_PASSWORD_SCREEN_ROUTE/{code}"
    const val HOME_SCREEN_ROUTE = "HOME_SCREEN_ROUTE"

    // Home Routes
    const val EXPANDED_SCREEN_ROUTE = "EXPANDED_SCREEN_ROUTE/{id}/{isLandmark}"
    const val LANDMARKS_LIST_SCREEN_ROUTE = "LANDMARKS_LIST_SCREEN_ROUTE"
    const val ARTIFACTS_LIST_SCREEN_ROUTE = "ARTIFACTS_LIST_SCREEN_ROUTE"
    const val MORE_REVIEWS_ROUTE = "MORE_REVIEWS_ROUTE"
    const val REVIEW_SCREEN_ROUTE= "REVIEW_SCREEN_ROUTE"
}