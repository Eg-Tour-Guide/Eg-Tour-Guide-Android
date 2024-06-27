package com.egtourguide.core.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordScreen
import com.egtourguide.auth.presentation.login.LoginScreen
import com.egtourguide.auth.presentation.otp.OtpScreen
import com.egtourguide.auth.presentation.resetPassword.ResetPasswordScreen
import com.egtourguide.auth.presentation.signup.SignUpScreen
import com.egtourguide.auth.presentation.welcome.WelcomeScreen
import com.egtourguide.home.presentation.screens.main.screens.artifacts_list.ArtifactsListScreen
import com.egtourguide.home.presentation.screens.expanded.ExpandedScreenRoot
import com.egtourguide.home.presentation.screens.expanded.ExpandedType
import com.egtourguide.home.presentation.screens.expanded.WebViewScreen
import com.egtourguide.home.presentation.screens.filter.FilterScreen
import com.egtourguide.home.presentation.screens.main.MainScreen
import com.egtourguide.home.presentation.screens.main.screens.home.HomeScreen
import com.egtourguide.home.presentation.screens.main.screens.landmarks_list.LandmarksListScreen
import com.egtourguide.home.presentation.screens.moreReviews.MoreReviewsScreenRoot
import com.egtourguide.home.presentation.screens.my_tours.MyToursScreen
import com.egtourguide.home.presentation.screens.review.ReviewScreen
import com.egtourguide.home.presentation.screens.saved_items.SavedScreen
import com.egtourguide.home.presentation.screens.search.SearchScreen
import com.egtourguide.home.presentation.screens.search_results.SearchResultsScreen
import com.egtourguide.home.presentation.screens.toursPlan.ToursPlanScreenRoot
import com.egtourguide.home.presentation.screens.main.screens.tours_list.ToursListScreen
import com.egtourguide.home.presentation.screens.main.screens.user.UserScreen
import com.google.gson.Gson

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        authGraph(navController = navController)

        composable(route = AppGraph.Main.route) {
            MainScreen()
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.Auth.route,
        startDestination = AppScreen.Welcome.route
    ) {
        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AppScreen.Login.route)
                }
            )
        }

        composable(route = AppScreen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(route = AppScreen.Login.route) {
                        popUpTo(route = AppScreen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToOTP = { code, name, email, phone, password ->
                    navController.navigate(
                        route = AppScreen.OTP.route
                            .replace("{code}", code)
                            .replace("{fromSignup}", "true")
                            .replace("{name}", name)
                            .replace("{email}", email)
                            .replace("{phone}", phone)
                            .replace("{password}", password)
                    )
                }
            )
        }

        composable(route = AppScreen.OTP.route) { entry ->
            val code = entry.arguments?.getString("code") ?: ""
            val fromSignup = entry.arguments?.getString("fromSignup").toBoolean()
            val name = entry.arguments?.getString("name") ?: ""
            val email = entry.arguments?.getString("email") ?: ""
            val phone = entry.arguments?.getString("phone") ?: ""
            val password = entry.arguments?.getString("password") ?: ""

            OtpScreen(
                code = code,
                fromSignup = fromSignup,
                name = name,
                email = email,
                phone = phone,
                password = password,
                onNavigateToResetPassword = {
                    navController.navigate(
                        route = AppScreen.ResetPassword.route.replace("{code}", code)
                    ) {
                        popUpTo(route = AppScreen.Login.route)
                    }
                },
                onNavigateToHome = {
                    navController.navigate(route = AppGraph.Main.route) {
                        popUpTo(route = AppScreen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
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
                    navController.navigate(route = AppGraph.Main.route) {
                        popUpTo(route = AppScreen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreen.ForgetPassword.route) {
            ForgotPasswordScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToOTP = { code ->
                    navController.navigate(
                        route = AppScreen.OTP.route.replace("{code}", code)
                    ) {
                        popUpTo(route = AppScreen.Login.route)
                    }
                }
            )
        }

        composable(route = AppScreen.ResetPassword.route) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            code?.let {
                ResetPasswordScreen(
                    code = it,
                    onNavigateToLogin = {
                        navController.navigate(route = AppScreen.Login.route) {
                            popUpTo(route = AppScreen.Login.route)
                        }
                    }
                )
            }
        }
    }
}

// TODO: Split it to multiple graphs!!
@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = AppGraph.Main.route,
        startDestination = AppScreen.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        composable(route = AppScreen.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToSinglePlace = { place ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", place.id)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                },
                onNavigateToDetectedArtifact = { artifact ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", artifact.id)
                            .replace("{expandedType}", ExpandedType.ARTIFACT.name)
                    )
                },
                onNavigateToEvent = { event ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", event.id)
                            .replace("{expandedType}", ExpandedType.EVENT.name)
                    )
                },
                onNavigateToSingleCategory = {
                    // TODO: Check about this!!
                }
            )
        }

        composable(route = AppScreen.ToursList.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            ToursListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "tour")
                            .replace("{QUERY}", "null")
                    )
                },
                onNavigateToSingleTour = { tour ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", tour.id)
                            .replace("{expandedType}", ExpandedType.TOUR.name)
                    )
                }
            )
        }

        composable(
            route = AppScreen.LandmarksList.route,
            arguments = listOf(
                navArgument("filters") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            LandmarksListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "landmark")
                            .replace("{QUERY}", "null")
                    )
                },
                onNavigateToSinglePlace = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                }
            )
        }

        composable(route = AppScreen.ArtifactsList.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            ArtifactsListScreen(
                filters = filters,
                onNavigateToSearch = {
                    navController.navigate(route = AppScreen.Search.route)
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "artifact")
                            .replace("{QUERY}", "null")
                    )
                },
                onNavigateToSingleArtifact = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", it.id)
                            .replace("{expandedType}", ExpandedType.ARTIFACT.name)
                    )
                }
            )
        }

        composable(route = AppScreen.User.route) {
            UserScreen()
        }

        // TODO here
        composable(route = AppScreen.Filter.route) { it ->
            val source = it.arguments?.getString("SOURCE")
            val query = it.arguments?.getString("QUERY")

            FilterScreen(
                source = source!!,
                query = query ?: "",
                onNavigateToResults = { hashMap ->
                    val hashMapJson = Gson().toJson(hashMap)
                    source.let {
                        when (it) {
                            "tour" -> {
                                navController.navigate(
                                    route = AppScreen.ToursList.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    popUpTo(AppScreen.Home.route)
                                }
                            }

                            "landmark" -> {
                                navController.navigate(
                                    route = AppScreen.LandmarksList.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    popUpTo(AppScreen.Home.route)
                                }
                            }

                            "search" -> {
                                navController.navigate(
                                    route = AppScreen.SearchResults.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    popUpTo(AppScreen.Search.route)
                                }
                            }

                            "saved" -> {
                                navController.navigate(
                                    route = AppScreen.Saved.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    //TODO change this to user when user screen is created
                                    popUpTo(AppScreen.Home.route)
                                }
                            }

                            "my_tours" -> {
                                navController.navigate(
                                    route = AppScreen.MyTours.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    popUpTo(AppScreen.MyTours.route)
                                }
                            }

                            "artifact" -> {
                                navController.navigate(
                                    route = AppScreen.ArtifactsList.route
                                        .replace("{filters}", hashMapJson)
                                ) {
                                    popUpTo(AppScreen.Home.route)
                                }
                            }
                        }
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = AppScreen.Expanded.route) { entry ->
            val id = entry.arguments?.getString("id") ?: ""
            val expandedType = entry.arguments?.getString("expandedType") ?: ""

            val tourId = entry.savedStateHandle.get<String>(key = "tourId") ?: ""
            val tourName = entry.savedStateHandle.get<String>(key = "tourName") ?: ""
            val tourImage = entry.savedStateHandle.get<String>(key = "tourImage") ?: ""

            ExpandedScreenRoot(
                id = id,
                tourId = tourId,
                tourName = tourName,
                tourImage = tourImage,
                expandedType = expandedType,
                onBackClicked = { navController.navigateUp() },
                onSeeMoreClicked = {
                    navController.navigate(route = AppScreen.MoreReviews.route)
                },
                onReviewClicked = {
                    navController.navigate(
                        route = AppScreen.Review.route
                            .replace("{id}", id)
                            .replace(
                                "{source}",
                                if (expandedType == ExpandedType.LANDMARK.name) "place" else "tour"
                            )
                    )
                },
                navigateToWebScreen = { modelUrl ->
                    navController.navigate(
                        route = AppScreen.WebView.route.replace(
                            "{modelUrl}",
                            modelUrl
                                .replace("/", "...")
                                .replace("?", "~~~")
                        )
                    )
                },
                navigateToTours = {
                    navController.navigate(
                        route = AppScreen.MyTours.route.replace("{isSelect}", "true")
                    )
                },
                goToTourPlan = { itemId ->
                    navController.navigate(
                        route = AppScreen.ToursPlan.route.replace("{tourId}", itemId)
                    )
                },
                navigateToSingleItem = { itemId, expandedType2 ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", itemId)
                            .replace("{expandedType}", expandedType2)
                    )
                }
            )
        }

        composable(route = AppScreen.WebView.route) { entry ->
            val modelUrl = entry.arguments?.getString("modelUrl") ?: ""

            WebViewScreen(
                modelUrl = modelUrl
                    .replace("...", "/")
                    .replace("~~~", "?")
            )
        }

        composable(route = AppScreen.MoreReviews.route) {
            MoreReviewsScreenRoot(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToReview = {
                    navController.navigate(route = AppScreen.Review.route)
                }
            )
        }

        composable(route = AppScreen.Review.route) { entry ->
            val itemId = entry.arguments?.getString("id") ?: ""
            val source = entry.arguments?.getString("source") ?: ""

            ReviewScreen(
                id = itemId,
                source = source,
                onNavigateBack = {
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", itemId)
                            .replace(
                                "{expandedType}",
                                if (source == "place") ExpandedType.LANDMARK.name else ExpandedType.TOUR.name
                            )
                    ) {
                        popUpTo(route = AppScreen.Expanded.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreen.Search.route) {
            SearchScreen(
                onNavigateToSearchResults = { query ->
                    navController.navigate(
                        route = AppScreen.SearchResults.route.replace("{query}", query)
                    )
                }
            )
        }

        composable(route = AppScreen.SearchResults.route) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString("query")
            val filtersJson = navBackStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            SearchResultsScreen(
                query = query ?: "",
                filters = filters,
                onNavigateToSearch = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { item ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", item.id)
                            .replace(
                                "{expandedType}",
                                if (item.isArtifact) ExpandedType.ARTIFACT.name else ExpandedType.LANDMARK.name
                            )
                    )
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "search")
                            .replace("{QUERY}", query ?: "")
                    )
                }
            )
        }

        composable(route = AppScreen.ToursPlan.route) { entry ->
            val tourID = entry.arguments?.getString("tourId") ?: ""

            ToursPlanScreenRoot(
                tourId = tourID,
                onBackClicked = {
                    navController.navigateUp()
                },
                navigateToLandmark = { landmarkId ->
                    navController.navigate(
                        route = AppScreen.Expanded.route
                            .replace("{id}", landmarkId)
                            .replace("{expandedType}", ExpandedType.LANDMARK.name)
                    )
                }
            )
        }

        composable(route = AppScreen.Saved.route) { backStackEntry ->
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            SavedScreen(
                filters = filters,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToSingleItem = { _ ->
                    //TODO handel navigation here
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "saved")
                            .replace("{QUERY}", "null")
                    )
                }
            )
        }

        composable(route = AppScreen.MyTours.route) { backStackEntry ->
            val isSelected = backStackEntry.arguments?.getBoolean("isSelect") ?: false
            val filtersJson = backStackEntry.arguments?.getString("filters")
            var filters: HashMap<*, *>? = null

            filtersJson?.let {
                filters = Gson().fromJson(
                    filtersJson.substringAfter('/'),
                    HashMap::class.java
                )
            }

            MyToursScreen(
                filters = filters,
                onNavigateToSingleTour = { tour ->
                    if (isSelected) {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourId",
                            tour.id
                        )
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourName",
                            tour.title
                        )
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "tourImage",
                            tour.image
                        )
                        navController.navigateUp()
                    } else {
                        // TODO: Navigate!!
                    }
                },
                onNavigateToFilters = {
                    navController.navigate(
                        route = AppScreen.Filter.route
                            .replace("{SOURCE}", "my_tours")
                            .replace("{QUERY}", "null")
                    )
                },
                onNavigateToCreateTour = {
                    //TODO navigate to create tour screen
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}