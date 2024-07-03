package com.egtourguide.core.presentation.navigation

object Routes {

    // Graphs
    const val AUTH_GRAPH_ROUTE = "AUTH_GRAPH_ROUTE"
    const val MAIN_GRAPH_ROUTE = "MAIN_GRAPH_ROUTE"
    const val EXPANDED_GRAPH_ROUTE = "EXPANDED_GRAPH_ROUTE/{id}/{expandedType}"
    const val USER_GRAPH_ROUTE = "USER_GRAPH_ROUTE"
    const val CUSTOM_TOURS_GRAPH_ROUTE = "CUSTOM_TOURS_GRAPH_ROUTE/{isSelect}"

    // Auth Routes
    const val WELCOME_SCREEN_ROUTE = "WELCOME_SCREEN_ROUTE"
    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"
    const val SIGNUP_SCREEN_ROUTE = "SIGNUP_SCREEN_ROUTE"
    const val OTP_SCREEN_ROUTE =
        "OTP_SCREEN_ROUTE/{code}/{fromSignup}/{name}/{email}/{phone}/{password}"
    const val FORGET_PASSWORD_SCREEN_ROUTE = "FORGET_PASSWORD_SCREEN_ROUTE"
    const val RESET_PASSWORD_SCREEN_ROUTE = "RESET_PASSWORD_SCREEN_ROUTE/{code}"

    // Home Routes
    const val HOME_SCREEN_ROUTE = "HOME_SCREEN_ROUTE"
    const val TOURS_SCREEN_ROUTE = "TOURS_SCREEN_ROUTE?filters={filters}"
    const val LANDMARKS_LIST_SCREEN_ROUTE = "LANDMARKS_LIST_SCREEN_ROUTE?filters={filters}"
    const val ARTIFACTS_LIST_SCREEN_ROUTE = "ARTIFACTS_LIST_SCREEN_ROUTE?filters={filters}"
    const val FILTER_SCREEN_ROUTE = "FILTER_SCREEN_ROUTE/{SOURCE}/{QUERY}"
    const val SEARCH_SCREEN_ROUTE = "SEARCH_SCREEN_ROUTE"
    const val SEARCH_RESULTS_SCREEN_ROUTE = "SEARCH_RESULTS_SCREEN_ROUTE/{query}?filters={filters}"

    // Expanded Routes
    const val EXPANDED_SCREEN_ROUTE = "EXPANDED_SCREEN_ROUTE"
    const val WEB_VIEW_SCREEN_ROUTE = "WEB_VIEW_SCREEN_ROUTE/{modelUrl}"
    const val REVIEW_SCREEN_ROUTE = "REVIEW_SCREEN_ROUTE"
    const val MORE_REVIEWS_ROUTE = "MORE_REVIEWS_ROUTE"
    const val TOURS_PLAN_SCREEN_ROUTE = "TOURS_PLAN_SCREEN_ROUTE/{tourId}"

    // User Routes
    const val USER_SCREEN_ROUTE = "USER_SCREEN_ROUTE"
    const val EDIT_PROFILE_SCREEN_ROUTE = "EDIT_PROFILE_SCREEN_ROUTE"
    const val CHANGE_PASSWORD_SCREEN_ROUTE = "CHANGE_PASSWORD_SCREEN_ROUTE"
    const val SETTINGS_SCREEN_ROUTE = "SETTINGS_SCREEN_ROUTE"
    const val SAVED_ITEMS_SCREEN_ROUTE = "SAVED_ITEMS_SCREEN_ROUTE?filters={filters}"

    // Custom Tours Routes
    const val MY_TOURS_SCREEN_ROUTE = "MY_TOURS_SCREEN_ROUTE/{isSelect}?filters={filters}"
    const val CREATE_TOUR_SCREEN_ROUTE = "CREATE_TOUR_SCREEN_ROUTE/{isCreate}/{tourId}/{name}/{description}"
}
