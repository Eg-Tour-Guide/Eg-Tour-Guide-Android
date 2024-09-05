package com.egtourguide.core.presentation.navigation

sealed class AppGraph(val route: String) {

    data object Auth : AppGraph(route = Routes.AUTH_GRAPH_ROUTE)
    data object Main : AppGraph(route = Routes.MAIN_GRAPH_ROUTE)
    data object Expanded : AppGraph(route = Routes.EXPANDED_GRAPH_ROUTE)
    data object User : AppGraph(route = Routes.USER_GRAPH_ROUTE)
    data object CustomTours : AppGraph(route = Routes.CUSTOM_TOURS_GRAPH_ROUTE)
}