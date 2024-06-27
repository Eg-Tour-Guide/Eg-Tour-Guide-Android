package com.egtourguide.core.presentation.navigation

sealed class AppScreen(val route: String) {

    // Auth Screens
    object Login : AppScreen(route = Routes.LOGIN_SCREEN_ROUTE)
    object SignUp : AppScreen(route = Routes.SIGNUP_SCREEN_ROUTE)
    object Welcome : AppScreen(route = Routes.WELCOME_SCREEN_ROUTE)
    object OTP : AppScreen(route = Routes.OTP_SCREEN_ROUTE)
    object ForgetPassword : AppScreen(route = Routes.FORGET_PASSWORD_SCREEN_ROUTE)
    object ResetPassword : AppScreen(route = Routes.RESET_PASSWORD_SCREEN_ROUTE)

    // Home Screens
    object Expanded : AppScreen(route = Routes.EXPANDED_SCREEN_ROUTE)
    object MoreReviews : AppScreen(route = Routes.MORE_REVIEWS_ROUTE)
    object Review : AppScreen(route = Routes.REVIEW_SCREEN_ROUTE)
    object Filter : AppScreen(route = Routes.FILTER_SCREEN_ROUTE)
    object WebView : AppScreen(route = Routes.WEB_VIEW_SCREEN_ROUTE)
    object Home : AppScreen(route = Routes.HOME_SCREEN_ROUTE)
    object ToursList : AppScreen(route = Routes.TOURS_SCREEN_ROUTE)
    object LandmarksList : AppScreen(route = Routes.LANDMARKS_LIST_SCREEN_ROUTE)
    object ArtifactsList : AppScreen(route = Routes.ARTIFACTS_LIST_SCREEN_ROUTE)
    object Search : AppScreen(route = Routes.SEARCH_SCREEN_ROUTE)
    object SearchResults : AppScreen(route = Routes.SEARCH_RESULTS_SCREEN_ROUTE)
    object ToursPlan : AppScreen(route = Routes.TOURS_PLAN_SCREEN_ROUTE)
    object MyTours : AppScreen(route = Routes.MY_TOURS_SCREEN_ROUTE)
    object Saved : AppScreen(route = Routes.SAVED_ITEMS_SCREEN_ROUTE)
    object User : AppScreen(route = Routes.USER_SCREEN_ROUTE)
}
