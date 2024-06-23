package com.egtourguide.core.presentation.navigation

sealed class AppScreen(val route: String) {

    // Auth Screens
    object Login : AppScreen(route = ScreenRoutes.LOGIN_SCREEN_ROUTE)
    object SignUp : AppScreen(route = ScreenRoutes.SIGNUP_SCREEN_ROUTE)
    object Welcome : AppScreen(route = ScreenRoutes.WELCOME_SCREEN_ROUTE)
    object OTP : AppScreen(route = ScreenRoutes.OTP_SCREEN_ROUTE)
    object ForgetPassword : AppScreen(route = ScreenRoutes.FORGET_PASSWORD_SCREEN_ROUTE)
    object ResetPassword : AppScreen(route = ScreenRoutes.RESET_PASSWORD_SCREEN_ROUTE)

    // Home Screens
    object Expanded : AppScreen(route = ScreenRoutes.EXPANDED_SCREEN_ROUTE)
    object MoreReviews : AppScreen(route = ScreenRoutes.MORE_REVIEWS_ROUTE)
    object Review : AppScreen(route = ScreenRoutes.REVIEW_SCREEN_ROUTE)
    object WebView : AppScreen(route = ScreenRoutes.WEB_VIEW_SCREEN_ROUTE)
    object Home : AppScreen(route = ScreenRoutes.HOME_SCREEN_ROUTE)
    object ToursList : AppScreen(route = ScreenRoutes.TOURS_SCREEN_ROUTE)
    object LandmarksList : AppScreen(route = ScreenRoutes.LANDMARKS_LIST_SCREEN_ROUTE)
    object ArtifactsList : AppScreen(route = ScreenRoutes.ARTIFACTS_LIST_SCREEN_ROUTE)
    object Search : AppScreen(route = ScreenRoutes.SEARCH_SCREEN_ROUTE)
    object ToursPlan : AppScreen(route = ScreenRoutes.TOURS_PLAN_SCREEN_ROUTE)
    object MyTours: AppScreen(route = ScreenRoutes.MY_TOURS_SCREEN_ROUTE)
}
