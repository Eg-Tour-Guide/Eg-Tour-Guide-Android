package com.egtourguide.core.presentation.navigation

sealed class AppScreen(val route: String) {

    // Auth Screens
    data object Welcome : AppScreen(route = Routes.WELCOME_SCREEN_ROUTE)
    data object Login : AppScreen(route = Routes.LOGIN_SCREEN_ROUTE)
    data object SignUp : AppScreen(route = Routes.SIGNUP_SCREEN_ROUTE)
    data object OTP : AppScreen(route = Routes.OTP_SCREEN_ROUTE)
    data object ForgetPassword : AppScreen(route = Routes.FORGET_PASSWORD_SCREEN_ROUTE)
    data object ResetPassword : AppScreen(route = Routes.RESET_PASSWORD_SCREEN_ROUTE)

    // Home Screens
    data object Home : AppScreen(route = Routes.HOME_SCREEN_ROUTE)
    data object ToursList : AppScreen(route = Routes.TOURS_SCREEN_ROUTE)
    data object LandmarksList : AppScreen(route = Routes.LANDMARKS_LIST_SCREEN_ROUTE)
    data object ArtifactsList : AppScreen(route = Routes.ARTIFACTS_LIST_SCREEN_ROUTE)
    data object Search : AppScreen(route = Routes.SEARCH_SCREEN_ROUTE)
    data object SearchResults : AppScreen(route = Routes.SEARCH_RESULTS_SCREEN_ROUTE)
    data object ToursFilter : AppScreen(route = Routes.TOURS_FILTER_SCREEN_ROUTE)
    data object LandmarksFilter : AppScreen(route = Routes.LANDMARKS_FILTER_SCREEN_ROUTE)
    data object ArtifactsFilter : AppScreen(route = Routes.ARTIFACTS_FILTER_SCREEN_ROUTE)
    data object SearchFilter : AppScreen(route = Routes.SEARCH_FILTER_SCREEN_ROUTE)

    // Expanded Screens
    data object Expanded : AppScreen(route = Routes.EXPANDED_SCREEN_ROUTE)
    data object WebView : AppScreen(route = Routes.WEB_VIEW_SCREEN_ROUTE)
    data object Review : AppScreen(route = Routes.REVIEW_SCREEN_ROUTE)
    data object MoreReviews : AppScreen(route = Routes.MORE_REVIEWS_ROUTE)
    data object ToursPlan : AppScreen(route = Routes.TOURS_PLAN_SCREEN_ROUTE)

    // User Screens
    data object User : AppScreen(route = Routes.USER_SCREEN_ROUTE)
    data object EditProfile : AppScreen(route = Routes.EDIT_PROFILE_SCREEN_ROUTE)
    data object ChangePassword : AppScreen(route = Routes.CHANGE_PASSWORD_SCREEN_ROUTE)
    data object Settings : AppScreen(route = Routes.SETTINGS_SCREEN_ROUTE)
    data object Saved : AppScreen(route = Routes.SAVED_SCREEN_ROUTE)
    data object SavedFilter : AppScreen(route = Routes.SAVED_FILTER_SCREEN_ROUTE)

    // Custom Tours Screens
    data object MyTours : AppScreen(route = Routes.MY_TOURS_SCREEN_ROUTE)
    data object CreateTour : AppScreen(route = Routes.CREATE_TOUR_SCREEN_ROUTE)
    data object CustomExpanded : AppScreen(route = Routes.CUSTOM_EXPANDED_SCREEN_ROUTE)
    data object MyToursFilter : AppScreen(route = Routes.MY_TOURS_FILTER_SCREEN_ROUTE)
    data object CustomToursPlan : AppScreen(route = Routes.CUSTOM_TOURS_PLAN_SCREEN_ROUTE)
}
