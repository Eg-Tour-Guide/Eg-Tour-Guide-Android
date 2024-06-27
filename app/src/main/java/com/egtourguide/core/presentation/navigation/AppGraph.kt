package com.egtourguide.core.presentation.navigation

sealed class AppGraph(val route: String) {

    object Auth : AppGraph(route = Routes.AUTH_GRAPH_ROUTE)
    object Main : AppGraph(route = Routes.MAIN_GRAPH_ROUTE)
    object Expanded: AppGraph(route = Routes.EXPANDED_GRAPH_ROUTE)
}